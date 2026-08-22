package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import java.time.LocalDateTime;

public record RoomTypeImageResponseDto(
        Long id,
        Long roomTypeId,
        String imageUrl,
        Integer displayOrder,
        LocalDateTime createdAt
) {
}