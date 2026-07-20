package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.BookingResponseDto;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.Booking;
import com.finartzIntern.HotelRezervationSys.domain.repository.BookingRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookingsByUserId(Long userId) {
        return bookingRepository.findAllByUser_Id(userId)
                .stream()
                .map(this::toBookingResponse)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getBookingByBookingNumber(String bookingNumber) {

        Booking booking = bookingRepository
                .findByBookingNumber(bookingNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found!"
                        )
                );

        return toBookingResponse(booking);
    }

    private BookingResponseDto toBookingResponse(Booking booking){
        return new BookingResponseDto(
                booking.getId(),
                booking.getUser().getId(),
                booking.getBookingNumber(),
                booking.getTotalAmount(),
                booking.getStatus(),
                booking.getPaymentStatus(),
                booking.getCreatedAt()
        );
    }
}
