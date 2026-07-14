package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelReviewRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelReviewService;

import java.util.List;

public class HotelReviewServiceImpl implements HotelReviewService {

    private final HotelReviewRepository hotelReviewRepository;
    public HotelReviewServiceImpl(HotelReviewRepository hotelReviewRepository){
        this.hotelReviewRepository = hotelReviewRepository;
    }
    @Override
    public List<HotelReviewResponseDto> getReviewsByHotelId(Long hotelId) {
        return hotelReviewRepository.findByHotelId(hotelId).stream().map(HotelReviewResponseDto::from).toList();
    }

    @Override
    public HotelReviewResponseDto getReviewByReservationId(Long reservationId) {
        return hotelReviewRepository.findByReservationId(reservationId).map(HotelReviewResponseDto::from).orElseThrow(() -> new RuntimeException("Review not found for reservation id: " + reservationId));
    }

    @Override
    public boolean hasReviewForReservation(Long reservationId) {
        return hotelReviewRepository.existsByReservationId(reservationId);
    }
}
