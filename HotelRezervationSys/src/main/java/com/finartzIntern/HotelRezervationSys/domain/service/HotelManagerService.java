package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelRegistrationRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelUpdateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelResponseDto;

public interface HotelManagerService {
    HotelResponseDto updateHotelInfo(Long id, HotelUpdateRequestDto updateDto);
    HotelResponseDto registerHotel(HotelRegistrationRequestDto dto);
}
