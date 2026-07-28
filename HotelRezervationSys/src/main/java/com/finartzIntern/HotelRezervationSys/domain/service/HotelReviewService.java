package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.CreateHotelReviewRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.UpdateHotelReviewRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewResponseDto;

import java.util.List;

public interface HotelReviewService {

    List<HotelReviewResponseDto> getReviewsByHotelId(Long hotelId);
    HotelReviewResponseDto getReviewById(Long reviewId);
    HotelReviewResponseDto getReviewByReservationId(Long reservationId);
    boolean hasReviewForReservation(Long reservationId);
    Double getAverageRatingByHotelId(Long hotelId);
    HotelReviewResponseDto createReview(Long hotelId, Long userId, Long reservationId, CreateHotelReviewRequestDto request);
    HotelReviewResponseDto updateReview(Long reviewId, UpdateHotelReviewRequestDto request);
    void deleteReview(Long reviewId);
}
