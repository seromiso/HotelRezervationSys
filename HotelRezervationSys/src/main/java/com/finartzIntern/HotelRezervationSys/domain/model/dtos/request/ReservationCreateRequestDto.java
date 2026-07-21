package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import java.time.LocalDate;

public record ReservationCreateRequestDto(
        Long hotelId,
        Long roomTypeId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer adultCount,
        Integer childCount
) {
}
