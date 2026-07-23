package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import java.util.List;

public record BookingCreateRequestDto(
        Long userId,
        ReservationCreateRequestDto reservation,
        List<ReservationGuestCreateRequestDto> guests
) {
}
