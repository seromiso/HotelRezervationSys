package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RoomPriceCreateRequestDto(
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal pricePerNight,
        String currency
) {
}
