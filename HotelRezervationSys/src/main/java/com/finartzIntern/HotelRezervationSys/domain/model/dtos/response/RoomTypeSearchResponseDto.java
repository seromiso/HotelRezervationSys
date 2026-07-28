package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import java.math.BigDecimal;
import java.util.List;

public record RoomTypeSearchResponseDto(
        Long id,
        String title,
        Integer maxAdults,
        Integer maxChildren,
        List<RoomTypeFeatureResponseDto> features,
        String coverImage,
        BigDecimal totalPrice,
        boolean isAvailable
) {
}
