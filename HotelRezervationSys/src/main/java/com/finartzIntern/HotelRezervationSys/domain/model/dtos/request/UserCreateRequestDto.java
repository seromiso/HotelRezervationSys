package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserRole;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserCreateRequestDto {
    private String email;
    private String password;
    private String name;
    private String surname;
    private UserRole role;
    private LocalDateTime dateBirth;
    private String phoneNumber;
}
