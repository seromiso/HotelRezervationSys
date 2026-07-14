package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.CreateRoomTypeRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeResponseDto;

import java.util.List;

public interface RoomTypeService {

    RoomTypeResponseDto createRoomType(Long hotelId, CreateRoomTypeRequestDto requestDto);
    List<RoomTypeResponseDto> getRoomTypesByHotelId(Long hotelId);

}
