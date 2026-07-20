package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotel-reviews")
public class HotelReviewController {
    private final HotelReviewService hotelReviewService;

    public HotelReviewController(HotelReviewService hotelReviewService){
        this.hotelReviewService = hotelReviewService;
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<HotelReviewResponseDto>> getReviewsByHotelId(@PathVariable Long hotelId){
        List<HotelReviewResponseDto> reviews = hotelReviewService.getReviewsByHotelId(hotelId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<HotelReviewResponseDto> getReviewByReservationId(
            @PathVariable Long reservationId) {
        HotelReviewResponseDto review = hotelReviewService.getReviewByReservationId(reservationId);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/reservation/{reservationId}/exists")
    public ResponseEntity<Boolean> hasReviewForReservation(
            @PathVariable Long reservationId) {
        boolean exists = hotelReviewService.hasReviewForReservation(reservationId);
        return ResponseEntity.ok(exists);
    }

}
