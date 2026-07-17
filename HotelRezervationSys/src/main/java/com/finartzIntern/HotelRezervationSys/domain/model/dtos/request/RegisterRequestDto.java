package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserRole;
import java.time.LocalDate;
public record RegisterRequestDto(
        String name,
        String surname,
        String email,
        String phone,
        String password,
        LocalDate birthDate

) {
}
