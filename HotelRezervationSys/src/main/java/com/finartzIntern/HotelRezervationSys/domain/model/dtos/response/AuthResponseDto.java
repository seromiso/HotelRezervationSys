package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserRole;

public record AuthResponseDto(
        String token,
        String email,
        UserRole role
) {
}
