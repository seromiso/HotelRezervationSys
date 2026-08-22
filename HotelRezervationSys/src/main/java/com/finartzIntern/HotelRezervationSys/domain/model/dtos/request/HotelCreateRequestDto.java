package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

public record HotelCreateRequestDto(
        Long id,
      Long managerId,
      String name,
      String city,
      String district,
      String address,
      String phone,
      String description,
      String ibanNo,
      String checkInTime,
     String checkOutTime)
{}
