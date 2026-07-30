package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.CreateHotelReviewRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.UpdateHotelReviewRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewListResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewSummaryResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HotelReviewService {

    List<HotelReviewResponseDto> getReviewsByHotelId(Long hotelId);
    HotelReviewResponseDto getReviewById(Long reviewId);
    HotelReviewResponseDto getReviewByReservationId(Long reservationId);
    boolean hasReviewForReservation(Long reservationId);
    Double getAverageRatingByHotelId(Long hotelId);
    Page<HotelReviewListResponseDto> getPublicReviewsByHotelId(Long hotelId, Pageable pageable);
    HotelReviewSummaryResponseDto getReviewSummaryByHotelId(Long hotelId);
    HotelReviewResponseDto createReview(Long hotelId, Long userId, Long reservationId, CreateHotelReviewRequestDto request);
    HotelReviewResponseDto createReviewForReservation(
            Long reservationId,
            CreateHotelReviewRequestDto request,
            User currentUser);
    HotelReviewResponseDto updateReview(Long reviewId, UpdateHotelReviewRequestDto request);
    void deleteReview(Long reviewId);

}
