package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.BookingDetailResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.OwnerBookingSummaryResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.BookingStatus;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface OwnerBookingService {

    Page<OwnerBookingSummaryResponseDto> getHotelBookings(
            Long hotelId,
            BookingStatus bookingStatus,
            ReservationStatus reservationStatus,
            LocalDate checkInFrom,
            LocalDate checkInTo,
            Pageable pageable
    );

    BookingDetailResponseDto getBookingDetail(
            Long hotelId,
            String bookingNumber
    );
}
