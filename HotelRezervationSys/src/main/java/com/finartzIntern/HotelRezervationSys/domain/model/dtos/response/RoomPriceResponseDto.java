package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record RoomPriceResponseDto(
        Long id,
        Long roomTypeId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal pricePerNight,
        String currency,
        LocalDateTime createdAt
) {
}
