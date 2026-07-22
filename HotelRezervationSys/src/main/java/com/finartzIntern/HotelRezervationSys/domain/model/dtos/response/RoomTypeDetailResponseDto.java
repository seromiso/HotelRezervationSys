package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.RoomTypeStatus;
import java.time.LocalDateTime;
import java.util.List;

public record RoomTypeDetailResponseDto(
        Long id,
        String title,
        String description, // Yeni eklenen açıklama
        Integer maxAdults,
        Integer maxChildren,
        Integer baseCapacity,
        String bedConfiguration,
        Integer totalInventory,
        RoomTypeStatus status,
        LocalDateTime createdAt,

        // İlişkili Tablolardan Gelecek Büyük Veriler
        List<RoomTypeImageResponseDto> gallery,
        List<RoomTypeFeatureResponseDto> features
) {
}