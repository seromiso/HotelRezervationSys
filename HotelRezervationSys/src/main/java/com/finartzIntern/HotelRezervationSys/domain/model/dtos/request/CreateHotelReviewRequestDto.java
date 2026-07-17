package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

public record CreateHotelReviewRequestDto(
        Integer rating,
        String comment
) {
}
