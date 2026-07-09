package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;
import lombok.Data;
import java.time.LocalDateTime;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserRole;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private UserRole role;
    private LocalDateTime birthDate;
    private String phoneNumber;
    private boolean email_verified;
    private String status;
    private LocalDateTime createdAt;
}
