package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelImageCreateRequestDto;

public interface HotelImageService {

    void deleteImageFromHotel(Long hotelId, Long imageId, Long currentUserId);
    void addImagesToHotel(Long hotelId, HotelImageCreateRequestDto requestDto, Long currentUserId);
}