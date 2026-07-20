package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.LoginRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RegisterRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.AuthResponseDto;

public interface AuthService {
    AuthResponseDto register(RegisterRequestDto registerRequestDto);
    AuthResponseDto login(LoginRequestDto loginRequestDto);
    void verifyEmail(String email);

}
