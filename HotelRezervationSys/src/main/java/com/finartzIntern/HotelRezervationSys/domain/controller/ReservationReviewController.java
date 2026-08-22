package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.CreateHotelReviewRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationReviewController {

    private final HotelReviewService hotelReviewService;

    public ReservationReviewController(HotelReviewService hotelReviewService) {
        this.hotelReviewService = hotelReviewService;
    }

    @PostMapping("/{reservationId}/review")
    public ResponseEntity<HotelReviewResponseDto> createReviewForReservation(
            @PathVariable Long reservationId,
            @Valid @RequestBody CreateHotelReviewRequestDto request,
            @AuthenticationPrincipal User currentUser
    ) {
        HotelReviewResponseDto createdReview =
                hotelReviewService.createReviewForReservation(
                        reservationId,
                        request,
                        currentUser
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }
}
