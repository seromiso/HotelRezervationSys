package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OwnerReservationDetailResponseDto(
        Long reservationId,
        String bookingNumber,
        Long hotelId,
        String hotelName,
        Long roomTypeId,
        String roomTypeTitle,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer adultCount,
        Integer childCount,
        BigDecimal pricePerNight,
        BigDecimal totalPrice,
        ReservationStatus status,
        List<ReservationGuestResponseDto> guests
) {
}
