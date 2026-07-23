package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.GuestType;

public record ReservationGuestResponseDto(
        Long id,
        String name,
        String surname,
        GuestType guestType,
        Boolean primaryGuest
) {
}
