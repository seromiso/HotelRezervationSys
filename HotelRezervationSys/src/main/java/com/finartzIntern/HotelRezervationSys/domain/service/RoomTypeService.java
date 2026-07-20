package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RoomTypeCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeResponseDto;

import java.util.List;

public interface RoomTypeService {

    RoomTypeResponseDto createRoomType(Long hotelId, RoomTypeCreateRequestDto requestDto);
    List<RoomTypeResponseDto> getRoomTypesByHotelId(Long hotelId);
    RoomTypeResponseDto getRoomTypeById(Long id);
    void deleteRoomType(Long id);

}
