package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

public record LoginRequestDto(
        String email,
        String password
) {
}
