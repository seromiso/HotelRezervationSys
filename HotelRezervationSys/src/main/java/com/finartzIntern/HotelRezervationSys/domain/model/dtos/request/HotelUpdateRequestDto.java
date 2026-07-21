package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

public record HotelUpdateRequestDto(
        String name,
        String city,
        String district,
        String address,
        String phone,
        String description,
        String ibanNo,
        String checkInTime,
        String checkOutTime
) {
}