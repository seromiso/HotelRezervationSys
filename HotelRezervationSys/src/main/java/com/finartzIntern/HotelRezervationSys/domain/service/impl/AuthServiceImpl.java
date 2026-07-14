package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.UserCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.UserResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserRole;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserStatus;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.YEARS;

/*public class AuthServiceImpl {
    public UserResponseDto registerUser(UserCreateRequestDto requestDto){

        if(userRepository.existsByEmail(requestDto.getEmail())){
            throw new RuntimeException("Bu e-posta adresi daha önce kullanılmış : " + requestDto.getEmail());
        }
        if (requestDto.getDateBirth() != null) {
            LocalDate doğumGünü = requestDto.getDateBirth().toLocalDate();
            LocalDate bugün = java.time.LocalDate.now();
            long yaş = YEARS.between(doğumGünü, bugün);

            if (yaş < 18) {
                throw new IllegalArgumentException("Sisteme kayıt olabilmek için en az 18 yaşında olmalısınız!");
            }
        }

        User user = userMapper.toEntity(requestDto);
        user.setRole(UserRole.CUSTOMER);
        user.setPasswordHash(requestDto.getPassword());
        user.setStatus(UserStatus.PENDING_APPROVAL);
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);

    }
}*/
