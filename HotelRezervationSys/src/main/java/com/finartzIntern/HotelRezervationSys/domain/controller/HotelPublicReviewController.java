package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewListResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewSummaryResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hotels/{hotelId}/reviews")
public class HotelPublicReviewController {

    private final HotelReviewService hotelReviewService;

    public HotelPublicReviewController(HotelReviewService hotelReviewService) {
        this.hotelReviewService = hotelReviewService;
    }

    @GetMapping
    public ResponseEntity<Page<HotelReviewListResponseDto>> getHotelReviews(
            @PathVariable Long hotelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HotelReviewListResponseDto> reviews =
                hotelReviewService.getPublicReviewsByHotelId(hotelId, pageable);

        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/summary")
    public ResponseEntity<HotelReviewSummaryResponseDto> getHotelReviewSummary(@PathVariable Long hotelId) {
        HotelReviewSummaryResponseDto summary = hotelReviewService.getReviewSummaryByHotelId(hotelId);

        return ResponseEntity.ok(summary);
    }
}
