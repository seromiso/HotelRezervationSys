package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewResponseDto;

import java.util.List;

public interface HotelReviewService {

    List<HotelReviewResponseDto> getReviewsByHotelId(Long hotelId);

    HotelReviewResponseDto getReviewByReservationId(Long reservationId);

    boolean hasReviewForReservation(Long reservationId);
}
