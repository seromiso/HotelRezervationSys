package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.RoomTypeStatus;
import java.time.LocalDateTime;

public record RoomTypeResponseDto(
        Long id,
        String title,
        Integer maxAdults,
        Integer maxChildren,
        Integer baseCapacity,
        String bedConfiguration,
        Integer totalInventory,
        RoomTypeStatus status,
        LocalDateTime createdAt
) {
}
