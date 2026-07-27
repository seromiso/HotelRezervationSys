package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

public record BookingHotelResponseDto (
        Long id,
        String name,
        String city,
        String district,
        String address,
        String phone,
        String checkInTime,
        String checkOutTime
){
}
