package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.BookingStatus;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BookingDetailResponseDto (
        Long id,
        String bookingNumber,
        BigDecimal totalAMount,
        BookingStatus status,
        PaymentStatus paymentStatus,
        LocalDateTime createdAt,
        List<ReservationDetailResponseDto> reservationDetails
) {
}
