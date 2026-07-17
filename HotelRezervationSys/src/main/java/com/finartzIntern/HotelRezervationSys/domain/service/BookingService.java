package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.BookingResponse;


import java.util.List;

public interface BookingService {
    List<BookingResponse> getBookingsByUserId(Long userId);
    BookingResponse getBookingByBookingNumber(String bookingNumber);
}
