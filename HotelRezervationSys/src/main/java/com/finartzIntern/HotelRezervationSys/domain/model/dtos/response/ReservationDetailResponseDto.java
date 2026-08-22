package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ReservationDetailResponseDto (
        Long id,
        BookingHotelResponseDto hotel,
        RoomTypeResponseDto roomType,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Long nightCount,
        Integer adultCount,
        Integer childCount,
        BigDecimal pricePerNight,
        BigDecimal totalPrice,
        ReservationStatus status,
        List<ReservationGuestResponseDto> guest
) {
}
