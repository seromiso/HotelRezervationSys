package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.BookingStatus;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OwnerBookingSummaryResponseDto(
        Long bookingId,
        String bookingNumber,
        String primaryGuestFullName,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer reservationCount,
        BigDecimal totalAmount,
        BookingStatus bookingStatus,
        PaymentStatus paymentStatus,
        LocalDateTime createdAt
) {
}
