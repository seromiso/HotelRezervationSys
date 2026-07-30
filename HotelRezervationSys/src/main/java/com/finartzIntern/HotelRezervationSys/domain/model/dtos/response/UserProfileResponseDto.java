package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserRole;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserStatus;

import java.time.LocalDate;

public record UserProfileResponseDto(
        Long id,
        String name,
        String surname,
        String email,
        String phone,
        LocalDate birthDate,
        UserRole role,
        UserStatus status
) {
}
