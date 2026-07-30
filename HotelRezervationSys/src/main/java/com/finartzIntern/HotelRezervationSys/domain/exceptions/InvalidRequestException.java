package com.finartzIntern.HotelRezervationSys.domain.exceptions;

/**
 * Thrown when a request fails validation or violates a business rule
 * that makes the request itself invalid -> HTTP 400.
 * Used across all services (booking, hotel, room type, review, etc.) —
 * the message parameter carries the context-specific detail.
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}