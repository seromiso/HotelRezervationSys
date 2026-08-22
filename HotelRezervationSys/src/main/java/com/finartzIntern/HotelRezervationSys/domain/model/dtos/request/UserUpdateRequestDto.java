package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

public record UserUpdateRequestDto(
    String name,
    String surname,
    String phone,
    String currentPassword,
    String newPassword){}

