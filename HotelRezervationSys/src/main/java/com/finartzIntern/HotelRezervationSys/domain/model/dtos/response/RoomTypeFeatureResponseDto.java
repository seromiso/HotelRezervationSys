package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.Features;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomTypeFeature;

import java.time.LocalDateTime;

public record RoomTypeFeatureResponseDto(Long id,
                                         Long roomTypeId,
                                         Long featureId,
                                         String featureName,
                                         String featureType,
                                         String categoryName,
                                         LocalDateTime createdAt) {

    // Senin yazdığın mevcut metot (Buna hiç dokunmuyoruz, olduğu gibi duruyor)
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

    // HotelServiceImpl tarafındaki hatayı çözen yeni "Method Overloading" kısmımız
    public static RoomTypeFeatureResponseDto from(Features feature) {
        return new RoomTypeFeatureResponseDto(
                null, // Ara tablo ID'si elimizde yok
                null, // RoomType ID'si şu an elimizde yok
                feature.getId(),
                feature.getName(),
                feature.getType(),
                feature.getCategory() != null ? feature.getCategory().getName() : null,
                null  // createdAt şu an elimizde yok
        );
    }
}