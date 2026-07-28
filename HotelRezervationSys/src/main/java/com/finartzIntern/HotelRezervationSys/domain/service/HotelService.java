package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelUpdateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeSearchResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.HotelStatus;

import java.time.LocalDate;
import java.util.List;

public interface HotelService  {
    HotelResponseDto getHotelById(Long Id);
    List<HotelResponseDto> listActiveHotels();
    List<HotelResponseDto> getHotelsByCityAndStatus(String city,HotelStatus status);
    List<HotelResponseDto> getHotelsByManagerId(Long id);
    List<HotelResponseDto> ListAllHotels();
    HotelResponseDto createHotel(HotelCreateRequestDto requestDto);
    HotelResponseDto updateHotelInfo(Long id, HotelUpdateRequestDto updateRequestDto);
    HotelResponseDto updateHotelStatus(Long id, HotelStatus status); //adminin onaylama işlemi
}
