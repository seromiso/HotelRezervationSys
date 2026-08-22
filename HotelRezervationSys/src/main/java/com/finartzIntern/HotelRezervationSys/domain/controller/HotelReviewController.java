package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.CreateHotelReviewRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.UpdateHotelReviewRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{reviewId}")
    public ResponseEntity<HotelReviewResponseDto> getReviewById(@PathVariable Long reviewId) {
        HotelReviewResponseDto review = hotelReviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<HotelReviewResponseDto> getReviewByReservationId(@PathVariable Long reservationId) {
        HotelReviewResponseDto review = hotelReviewService.getReviewByReservationId(reservationId);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/reservation/{reservationId}/exists")
    public ResponseEntity<Boolean> hasReviewForReservation(@PathVariable Long reservationId) {
        boolean exists = hotelReviewService.hasReviewForReservation(reservationId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/hotel/{hotelId}/average-rating")
    public ResponseEntity<Double> getAverageRatingByHotelId(@PathVariable Long hotelId) {
        Double averageRating = hotelReviewService.getAverageRatingByHotelId(hotelId);
        return ResponseEntity.ok(averageRating);
    }

    @PostMapping("/hotel/{hotelId}/user/{userId}/reservation/{reservationId}")
    public ResponseEntity<HotelReviewResponseDto> createReview(
            @PathVariable Long hotelId,
            @PathVariable Long userId,
            @PathVariable Long reservationId,
            @Valid @RequestBody CreateHotelReviewRequestDto request) {
        HotelReviewResponseDto createdReview = hotelReviewService.createReview(
                hotelId,
                userId,
                reservationId,
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<HotelReviewResponseDto> updateReview(@PathVariable Long reviewId, @Valid @RequestBody UpdateHotelReviewRequestDto request) {
        HotelReviewResponseDto updatedReview = hotelReviewService.updateReview(reviewId, request);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId
    ) {
        hotelReviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

}
