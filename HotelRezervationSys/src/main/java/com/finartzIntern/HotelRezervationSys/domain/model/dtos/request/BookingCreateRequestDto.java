package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BookingCreateRequestDto(

        @NotNull
        Long userId,

        @NotEmpty
        List<@Valid ReservationCreateRequestDto> reservations

) {
}
