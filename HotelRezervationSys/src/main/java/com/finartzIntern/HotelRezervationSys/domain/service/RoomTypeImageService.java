package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RoomTypeImageCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeImageResponseDto;

import java.util.List;

public interface RoomTypeImageService {

    RoomTypeImageResponseDto addImageToRoomType(Long roomTypeId, RoomTypeImageCreateRequestDto requestDto);
    List<RoomTypeImageResponseDto> getImagesByRoomTypeId(Long roomTypeId);
    void deleteImage(Long imageId);
}