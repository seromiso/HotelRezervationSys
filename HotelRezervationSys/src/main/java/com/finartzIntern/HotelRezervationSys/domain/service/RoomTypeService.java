package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RoomTypeCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeDetailResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeSearchResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface RoomTypeService {

    RoomTypeResponseDto createRoomType(Long hotelId, RoomTypeCreateRequestDto requestDto);
    List<RoomTypeResponseDto> getRoomTypesByHotelId(Long hotelId);
    List<RoomTypeSearchResponseDto> searchRoomTypes(Long hotelId, LocalDate checkIn, LocalDate checkOut, Integer adults, Integer children);
    RoomTypeDetailResponseDto getRoomTypeById(Long id);
    RoomTypeResponseDto updateRoomType(Long id, RoomTypeCreateRequestDto requestDto);
    void deleteRoomType(Long id);

}