package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserRole;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
public class UserCreateRequestDto {
    private String email;
    private String password;
    private String name;
    private String surname;
    private UserRole role;
    private LocalDate dateBirth;
    private String phoneNumber;
}
