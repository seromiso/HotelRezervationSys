package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RoomPriceCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomPriceResponseDto;

import java.util.List;

public interface RoomPriceService {
    RoomPriceResponseDto addRoomPrice(Long roomTypeId, RoomPriceCreateRequestDto requestDto);
    List<RoomPriceResponseDto> getRoomPricesByRoomTypeId(Long roomTypeId);
    void deleteRoomPrice(Long id);
}
