package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewListResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/owner/hotels/{hotelId}/reviews")
public class OwnerHotelReviewController {

    private final HotelReviewService hotelReviewService;

    public OwnerHotelReviewController(HotelReviewService hotelReviewService) {
        this.hotelReviewService = hotelReviewService;
    }

    @GetMapping
    public ResponseEntity<Page<HotelReviewListResponseDto>> getHotelReviews(
            @PathVariable Long hotelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User currentUser
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<HotelReviewListResponseDto> reviews =
                hotelReviewService.getHotelReviewsAsOwner(
                        hotelId,
                        pageable,
                        currentUser
                );

        return ResponseEntity.ok(reviews);
    }
}