package com.finartzIntern.HotelRezervationSys.domain.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

/**
 * Central handler that catches exceptions across the app and converts them
 * into a consistent ErrorResponse. Applied automatically to every controller.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Requested resource (user, hotel, reservation, etc.) was not found -> 404.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
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
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid data type! Please provide a valid value.",
                request
        );
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
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred: " + ex.getMessage(),
                request
        );
    }

    /**
     * Shared helper that builds the ErrorResponse and wraps it in a ResponseEntity.
     * New handlers just need to call this with a status and a message.
     */
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
}