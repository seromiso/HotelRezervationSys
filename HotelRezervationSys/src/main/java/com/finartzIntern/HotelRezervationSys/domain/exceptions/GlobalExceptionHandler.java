package com.finartzIntern.HotelRezervationSys.domain.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Locale;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Resource not found (user, hotel, reservation, etc.) -> 404.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        String message = resolveMessageWithFallback(
                ex.getMessage(), "error.resource.not.found", request.getLocale());
        return buildResponse(HttpStatus.NOT_FOUND, message, request);
    }

    /**
     * Request failed validation or violates a business rule -> 400.
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(
            InvalidRequestException ex,
            HttpServletRequest request
    ) {
        String message = resolveMessageWithFallback(
                ex.getMessage(), "error.invalid.request", request.getLocale());
        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    /**
     * Request valid but conflicts with current state -> 409.
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(
            ConflictException ex,
            HttpServletRequest request
    ) {
        String message = resolveMessageWithFallback(
                ex.getMessage(), "error.conflict", request.getLocale());
        return buildResponse(HttpStatus.CONFLICT, message, request);
    }

    /**
     * BadRequestException & IllegalArgumentException -> 400.
     * Bu iki exception aynı işlemi yaptığı için tek metotta birleştirildi.
     */
    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleBadRequests(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Wrong type in a path/request parameter -> 400.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        String message = resolveMessage("error.type.mismatch", request.getLocale());
        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    /**
     * Authentication (Giriş Başarısız) -> 401
     */
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            org.springframework.security.core.AuthenticationException ex,
            HttpServletRequest request
    ) {
        // Özel hata başlığı (error parameter) kullanabilmek için overloaded metodu çağırıyoruz.
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                "Giriş Başarısız",
                "E-posta veya şifre hatalı! (Detay: " + ex.getMessage() + ")",
                request
        );
    }

    /**
     * Email Not Verified (E-posta Onaylanmamış) -> 403
     */
    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotVerifiedException(
            EmailNotVerifiedException ex,
            HttpServletRequest request
    ) {
        // Frontend'in popup açması için "EMAIL_NOT_VERIFIED" stringini özel gönderiyoruz
        return buildResponse(
                HttpStatus.FORBIDDEN,
                "EMAIL_NOT_VERIFIED",
                ex.getMessage(),
                request
        );
    }

    /**
     * Catch-all for anything not handled above -> 500.
     * Must stay last.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            HttpServletRequest request
    ) {
        String message = resolveMessage("error.internal.server", request.getLocale());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, request);
    }

    // --- HELPER METOTLAR ---

    /**
     * Standart HTTP Status Error'ları için yardımcı metot
     */
    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        return buildResponse(status, status.getReasonPhrase(), message, request);
    }

    /**
     * Özel Hata Tipleri (Örn: "EMAIL_NOT_VERIFIED" veya "Giriş Başarısız") için yardımcı metot
     */
    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String errorType,
            String message,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                errorType,
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(errorResponse);
    }

    private String resolveMessage(String key, Locale locale) {
        try {
            return messageSource.getMessage(key, null, locale);
        } catch (NoSuchMessageException e) {
            return key;
        }
    }

    private String resolveMessageWithFallback(String specificKey, String fallbackKey, Locale locale) {
        try {
            return messageSource.getMessage(specificKey, null, locale);
        } catch (NoSuchMessageException e) {
            return resolveMessage(fallbackKey, locale);
        }
    }
}


