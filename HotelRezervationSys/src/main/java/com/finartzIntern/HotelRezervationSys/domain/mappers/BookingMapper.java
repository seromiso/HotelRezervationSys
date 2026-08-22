package com.finartzIntern.HotelRezervationSys.domain.mappers;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.BookingDetailResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.BookingHotelResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.BookingResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.ReservationDetailResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.ReservationGuestResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Booking;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Reservation;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.ReservationGuest;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomType;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class BookingMapper {

    public BookingResponseDto toBookingResponse(
            Booking booking
    ) {
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

    public BookingDetailResponseDto toBookingDetailResponse(
            Booking booking,
            List<ReservationDetailResponseDto> reservations
    ) {
        return new BookingDetailResponseDto(
                booking.getId(),
                booking.getBookingNumber(),
                booking.getTotalAmount(),
                booking.getStatus(),
                booking.getPaymentStatus(),
                booking.getCreatedAt(),
                reservations
        );
    }

    public BookingDetailResponseDto toBookingDetailResponse(
            Booking booking
    ) {
        List<ReservationDetailResponseDto> reservationDetails =
                booking.getReservations()
                        .stream()
                        .map(this::toReservationDetailResponse)
                        .toList();

        return new BookingDetailResponseDto(
                booking.getId(),
                booking.getBookingNumber(),
                booking.getTotalAmount(),
                booking.getStatus(),
                booking.getPaymentStatus(),
                booking.getCreatedAt(),
                reservationDetails
        );
    }

    public ReservationDetailResponseDto toReservationDetailResponse(
            Reservation reservation
    ) {
        List<ReservationGuestResponseDto> guestResponses =
                reservation.getGuests()
                        .stream()
                        .map(this::toReservationGuestResponse)
                        .toList();

        long nightCount = ChronoUnit.DAYS.between(
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );

        return new ReservationDetailResponseDto(
                reservation.getId(),
                toHotelResponse(reservation.getHotel()),
                toRoomTypeResponse(reservation.getRoomType()),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                nightCount,
                reservation.getAdultCount(),
                reservation.getChildCount(),
                reservation.getPricePerNight(),
                reservation.getTotalPrice(),
                reservation.getStatus(),
                guestResponses
        );
    }

    public ReservationDetailResponseDto toReservationDetailResponse(
            Reservation reservation,
            List<ReservationGuest> guests
    ) {
        List<ReservationGuestResponseDto> guestResponses =
                guests.stream()
                        .map(this::toReservationGuestResponse)
                        .toList();

        long nightCount =
                ChronoUnit.DAYS.between(
                        reservation.getCheckInDate(),
                        reservation.getCheckOutDate()
                );

        return new ReservationDetailResponseDto(
                reservation.getId(),
                toHotelResponse(reservation.getHotel()),
                toRoomTypeResponse(reservation.getRoomType()),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                nightCount,
                reservation.getAdultCount(),
                reservation.getChildCount(),
                reservation.getPricePerNight(),
                reservation.getTotalPrice(),
                reservation.getStatus(),
                guestResponses
        );
    }

    public ReservationGuestResponseDto toReservationGuestResponse(
            ReservationGuest guest
    ) {
        return new ReservationGuestResponseDto(
                guest.getId(),
                guest.getName(),
                guest.getSurname(),
                guest.getGuestType(),
                guest.getPrimaryGuest()
        );
    }

    public BookingHotelResponseDto toHotelResponse(
            Hotel hotel
    ) {
        return new BookingHotelResponseDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getCity(),
                hotel.getDistrict(),
                hotel.getAddress(),
                hotel.getPhone(),
                hotel.getCheckInTime(),
                hotel.getCheckOutTime()
        );
    }

    public RoomTypeResponseDto toRoomTypeResponse(
            RoomType roomType
    ) {
        return new RoomTypeResponseDto(
                roomType.getId(),
                roomType.getTitle(),
                roomType.getMaxAdults(),
                roomType.getMaxChildren(),
                roomType.getBaseCapacity(),
                roomType.getBedConfiguration(),
                roomType.getTotalInventory(),
                roomType.getStatus(),
                roomType.getCreatedAt()
        );
    }
}