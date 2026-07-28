package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.BookingDetailResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.OwnerBookingSummaryResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.BookingStatus;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.ReservationStatus;
import com.finartzIntern.HotelRezervationSys.domain.service.OwnerBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/owner")
@RequiredArgsConstructor
public class OwnerBookingController {

    private final OwnerBookingService ownerBookingService;

    @GetMapping("/hotels/{hotelId}/bookings")
    public Page<OwnerBookingSummaryResponseDto> getHotelBookings(
            @PathVariable Long hotelId,

            @RequestParam(required = false)
            BookingStatus bookingStatus,

            @RequestParam(required = false)
            ReservationStatus reservationStatus,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkInFrom,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkInTo,

            Pageable pageable
    ) {
        return ownerBookingService.getHotelBookings(
                hotelId,
                bookingStatus,
                reservationStatus,
                checkInFrom,
                checkInTo,
                pageable
        );
    }

    @GetMapping("/hotels/{hotelId}/bookings/{bookingNumber}")
    public BookingDetailResponseDto getBookingDetail(
            @PathVariable Long hotelId,
            @PathVariable String bookingNumber
    ) {
        return ownerBookingService.getBookingDetail(
                hotelId,
                bookingNumber
        );
    }
}