package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserRole;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private UserRole role;
    private LocalDate birthDate;
    private String phoneNumber;
    private boolean email_verified;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
