package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.AddRoomTypeFeatureRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeFeatureResponseDto;

import java.util.List;

public interface RoomTypeFeatureService {

    List<RoomTypeFeatureResponseDto> getFeaturesByRoomTypeId(Long roomTypeId);
    RoomTypeFeatureResponseDto getRoomTypeFeature(Long roomTypeId, Long featureId);

    boolean hasFeature(Long roomTypeId, Long featureId);

    RoomTypeFeatureResponseDto addFeatureToRoomType(
            Long roomTypeId,
            AddRoomTypeFeatureRequestDto request
    );

    void removeFeatureFromRoomType(Long roomTypeId, Long featureId);
}
