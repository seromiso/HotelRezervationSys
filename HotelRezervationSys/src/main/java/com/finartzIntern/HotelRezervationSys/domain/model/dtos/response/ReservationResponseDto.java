package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationResponseDto(
        Long id,
        Long hotelId,
        Long roomTypeId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer adultCount,
        Integer childCount,
        BigDecimal pricePerNight,
        BigDecimal totalPrice,
        ReservationStatus status
) {
}
