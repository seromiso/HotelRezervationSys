package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomTypeFeature;

import java.time.LocalDateTime;

public record RoomTypeFeatureResponseDto(Long id,
                                         Long roomTypeId,
                                         Long featureId,
                                         String featureName,
                                         String featureType,
                                         String categoryName,
                                         LocalDateTime createdAt) {

    public static RoomTypeFeatureResponseDto from(RoomTypeFeature roomTypeFeature) {
        return new RoomTypeFeatureResponseDto(
                roomTypeFeature.getId(),
                roomTypeFeature.getRoomType().getId(),
                roomTypeFeature.getFeature().getId(),
                roomTypeFeature.getFeature().getName(),
                roomTypeFeature.getFeature().getType(),
                roomTypeFeature.getFeature().getCategory().getName(),
                roomTypeFeature.getCreatedAt()
        );
    }
}
