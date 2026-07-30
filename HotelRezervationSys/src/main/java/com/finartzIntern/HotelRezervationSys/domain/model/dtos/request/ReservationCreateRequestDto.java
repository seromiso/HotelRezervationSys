package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record ReservationCreateRequestDto(

        @NotNull
        Long hotelId,

        @NotNull
        Long roomTypeId,

        @NotNull
        @FutureOrPresent
        LocalDate checkInDate,

        @NotNull
        @Future
        LocalDate checkOutDate,

        @NotNull
        @Min(1)
        Integer adultCount,

        @Min(0)
        Integer childCount,

        @NotEmpty
        List<@Valid ReservationGuestCreateRequestDto> guests

) {
}
