package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.BookingCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.BookingDetailResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.BookingResponseDto;


import java.util.List;

public interface BookingService {
    List<BookingResponseDto> getBookingsByUserId(Long userId);
    BookingResponseDto createBooking(BookingCreateRequestDto bookingCreateRequestDto);
    BookingDetailResponseDto getBookingDetailByBookingNumber(
            String bookingNumber
    );
}
