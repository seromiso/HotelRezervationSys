package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.mappers.BookingMapper;
import com.finartzIntern.HotelRezervationSys.domain.mappers.OwnerBookingMapper;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.BookingDetailResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.OwnerBookingSummaryResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Booking;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.BookingStatus;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.ReservationStatus;
import com.finartzIntern.HotelRezervationSys.domain.repository.BookingRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.ReservationRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.OwnerBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OwnerBookingServiceImpl implements OwnerBookingService {

    private final BookingRepository bookingRepository;
    private final OwnerBookingMapper ownerBookingMapper;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<OwnerBookingSummaryResponseDto> getHotelBookings(
            Long hotelId,
            BookingStatus bookingStatus,
            ReservationStatus reservationStatus,
            LocalDate checkInFrom,
            LocalDate checkInTo,
            Pageable pageable
    ) {

        Page<Booking> bookings = bookingRepository.findOwnerHotelBookings(
                hotelId,
                bookingStatus,
                reservationStatus,
                checkInFrom,
                checkInTo,
                pageable
        );

        return bookings.map(ownerBookingMapper::toSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDetailResponseDto getBookingDetail(
            Long hotelId,
            String bookingNumber
    ) {
        Booking booking = bookingRepository
                .findOwnerBookingDetail(hotelId, bookingNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "error.booking.not.found"
                        )
                );

        return bookingMapper.toBookingDetailResponse(booking);
    }
}
