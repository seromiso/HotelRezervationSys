package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;


import com.finartzIntern.HotelRezervationSys.domain.model.entities.HotelImage;

public record HotelSearchResponseDto(
        Long id,
        String name,
        String city,
        String district,
        String address,
        Double AverageRating,
        String image
) {
}
