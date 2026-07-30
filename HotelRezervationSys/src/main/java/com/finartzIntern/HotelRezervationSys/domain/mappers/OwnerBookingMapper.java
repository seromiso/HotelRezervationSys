package com.finartzIntern.HotelRezervationSys.domain.mappers;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.OwnerBookingSummaryResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Booking;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Reservation;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.ReservationGuest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class OwnerBookingMapper {

    public OwnerBookingSummaryResponseDto toSummaryDto(Booking booking) {

        List<Reservation> reservations = booking.getReservations();

        LocalDate checkInDate = reservations.stream()
                .map(Reservation::getCheckInDate)
                .min(LocalDate::compareTo)
                .orElse(null);

        LocalDate checkOutDate = reservations.stream()
                .map(Reservation::getCheckOutDate)
                .max(LocalDate::compareTo)
                .orElse(null);

        String primaryGuestFullName = reservations.stream()
                .flatMap(reservation -> reservation.getGuests().stream())
                .filter(ReservationGuest::getPrimaryGuest)
                .findFirst()
                .map(guest -> guest.getName() + " " + guest.getSurname())
                .orElse(null);

        return new OwnerBookingSummaryResponseDto(
                booking.getId(),
                booking.getBookingNumber(),
                primaryGuestFullName,
                checkInDate,
                checkOutDate,
                reservations.size(),
                booking.getTotalAmount(),
                booking.getStatus(),
                booking.getPaymentStatus(),
                booking.getCreatedAt()
        );
    }
}