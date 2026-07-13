package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.UserCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto getUserById(Long id);
    UserResponseDto getUserByEmail(String email);
    UserResponseDto createUser(UserCreateRequestDto userRequestDto);
    List<UserResponseDto> getAllUsers();
}
