package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.BookingCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.BookingDetailResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.BookingResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/users/{userId}/bookings")
    public List<BookingResponseDto> getBookingsByUserId(@PathVariable Long userId) {
        return bookingService.getBookingsByUserId(userId);
    }

    @GetMapping("/bookings/{bookingNumber}")
    public ResponseEntity<BookingDetailResponseDto> getBookingDetailByBookingNumber(
            @PathVariable String bookingNumber
    ) {
        return ResponseEntity.ok(
                bookingService.getBookingDetailByBookingNumber(bookingNumber)
        );
    }

    @PostMapping("/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto createBooking(
            @RequestBody BookingCreateRequestDto request
    ) {
        return bookingService.createBooking(request);
    }
}
