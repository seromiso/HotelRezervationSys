package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.GuestType;

public record ReservationGuestCreateRequestDto(
        String name,
        String surname,
        GuestType guestType,
        Boolean primaryGuest
) {
}
