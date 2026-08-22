package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.Features;

import java.util.Set;

public record HotelDetailResponseDto(
        Long id,
        String name,
        String city,
        String district,
        String address,
        String description,
        Set<String> features,
        Double averageRating

) {
}
