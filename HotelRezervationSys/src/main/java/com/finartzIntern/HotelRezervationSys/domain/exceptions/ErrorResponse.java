package com.finartzIntern.HotelRezervationSys.domain.exceptions;

import java.time.LocalDateTime;

/**
 * Standard error response returned by the API.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
