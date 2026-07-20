package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

public record RoomTypeImageCreateRequestDto(
        String imageUrl,
        Integer displayOrder
) {
}