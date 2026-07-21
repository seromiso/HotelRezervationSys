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
     * ex.getMessage() is expected to be a message key (e.g. "user.not.found").
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
     * ex.getMessage() is expected to be a message key (e.g. "booking.checkin.past").
     * Shared across all services.
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
     * ex.getMessage() is expected to be a message key (e.g. "booking.no.availability").
     * Shared across all services.
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
     * Wrong type in a path/request parameter (e.g. text instead of a number) -> 400.
     * Thrown by Spring itself, not a custom exception.
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
     * Catch-all for anything not handled above -> 500.
     * Keeps the app from crashing with a raw stack trace. Must stay last.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            HttpServletRequest request
    ) {
        String message = resolveMessage("error.internal.server", request.getLocale());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
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

    /**
     * Resolves a specific message key and falls back to a generic key
     * if the specific one isn't defined in messages.properties.
     * Used for exceptions where the service picks a specific key per case.
     */
    private String resolveMessageWithFallback(String specificKey, String fallbackKey, Locale locale) {
        try {
            return messageSource.getMessage(specificKey, null, locale);
        } catch (NoSuchMessageException e) {
            return resolveMessage(fallbackKey, locale);
        }
    }
}