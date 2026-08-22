package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ConflictException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.InvalidRequestException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.mappers.BookingMapper;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.BookingCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.ReservationCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.ReservationGuestCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.*;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.*;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.BookingStatus;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.PaymentStatus;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.ReservationStatus;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.RoomTypeStatus;
import com.finartzIntern.HotelRezervationSys.domain.repository.*;
import com.finartzIntern.HotelRezervationSys.domain.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationGuestRepository reservationGuestRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomPriceRepository roomPriceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookingsByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "error.user.not.found"
            );
        }

        return bookingRepository.findAllByUser_Id(userId)
                .stream()
                .map(bookingMapper::toBookingResponse)
                .toList();
    }


    @Override
    @Transactional
    public BookingResponseDto createBooking(
            BookingCreateRequestDto request
    ) {
        User currentUser = findUser(request.userId());

        validateReservationsNotEmpty(
                request.reservations()
        );

        validateAllReservationsBelongToSameHotel(
                request.reservations()
        );

        Hotel hotel = findHotel(
                request.reservations()
                        .getFirst()
                        .hotelId()
        );

        List<PreparedReservation> preparedReservations =
                request.reservations()
                        .stream()
                        .map(reservationRequest ->
                                prepareReservation(
                                        hotel,
                                        reservationRequest
                                )
                        )
                        .toList();

        validateAllReservationsHaveSameDates(
                request.reservations()
        );

        validateRequestedRoomAvailability(
                request.reservations()
        );

        BigDecimal bookingTotalAmount =
                calculateBookingTotal(
                        preparedReservations
                );

        Booking savedBooking =
                saveBooking(
                        currentUser,
                        bookingTotalAmount
                );

        preparedReservations.forEach(preparedReservation -> {
            Reservation savedReservation =
                    saveReservation(
                            savedBooking,
                            hotel,
                            preparedReservation.roomType(),
                            preparedReservation.roomPrice(),
                            preparedReservation.request(),
                            preparedReservation.totalPrice()
                    );

            saveReservationGuests(
                    savedReservation,
                    preparedReservation.request().guests()
            );
        });

        return bookingMapper.toBookingResponse(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDetailResponseDto getBookingDetailByBookingNumber(
            String bookingNumber
    ) {
        Booking booking = bookingRepository
                .findByBookingNumber(bookingNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "error.booking.not.found"
                        )
                );

        List<Reservation> reservations =
                reservationRepository.findAllByBooking_Id(
                        booking.getId()
                );

        List<ReservationDetailResponseDto> reservationResponses =
                reservations.stream()
                        .map(reservation -> {
                            List<ReservationGuest> guests =
                                    reservationGuestRepository
                                            .findAllByReservation_Id(
                                                    reservation.getId()
                                            );

                            return bookingMapper.toReservationDetailResponse(
                                    reservation,
                                    guests
                            );
                        })
                        .toList();

        return bookingMapper.toBookingDetailResponse(booking, reservationResponses);
    }

    private BigDecimal calculateTotalPrice(
            RoomPrice roomPrice,
            LocalDate checkInDate,
            LocalDate checkOutDate
    ) {
        long numberOfNights =
                ChronoUnit.DAYS.between(
                        checkInDate,
                        checkOutDate
                );

        return roomPrice.getPricePerNight()
                .multiply(
                        BigDecimal.valueOf(numberOfNights)
                );
    }

    //createbooking helper methods
    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "error.user.not.found"
                        )
                );
    }

    private Hotel findHotel(Long hotelId) {
        return hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "error.hotel.not.found"
                        )
                );
    }

    private RoomType findRoomType(Long roomTypeId) {
        return roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "error.room.type.not.found"
                        )
                );
    }

    private void validateRoomTypeBelongsToHotel(
            RoomType roomType,
            Hotel hotel
    ) {
        if (!roomType.getHotel().getId().equals(hotel.getId())) {
            throw new InvalidRequestException(
                    "error.booking.invalid.selection"
            );
        }
    }

    private void validateRequestedRoomAvailability(
            List<ReservationCreateRequestDto> reservations
    ) {
        Map<RoomAvailabilityKey, Long> requestedRoomCounts =
                reservations.stream()
                        .collect(
                                Collectors.groupingBy(
                                        reservation ->
                                                new RoomAvailabilityKey(
                                                        reservation.roomTypeId(),
                                                        reservation.checkInDate(),
                                                        reservation.checkOutDate()
                                                ),
                                        Collectors.counting()
                                )
                        );

        requestedRoomCounts.forEach((key, requestedRoomCount) -> {
            RoomType roomType =
                    findRoomType(key.roomTypeId());

            long reservedRoomCount =
                    reservationRepository
                            .countOverlappingReservations(
                                    key.roomTypeId(),
                                    key.checkInDate(),
                                    key.checkOutDate(),
                                    ReservationStatus.CANCELLED
                            );

            long availableRoomCount =
                    roomType.getTotalInventory()
                            - reservedRoomCount;

            if (requestedRoomCount > availableRoomCount) {
                throw new ConflictException(
                        "error.booking.insufficient.room.availability"
                );
            }
        });
    }

    private void validateDates(
            LocalDate checkInDate,
            LocalDate checkOutDate
    ) {
        if (checkInDate == null || checkOutDate == null) {
            throw new InvalidRequestException(
                    "error.booking.dates.required"
            );
        }

        if (checkInDate.isBefore(LocalDate.now())) {
            throw new InvalidRequestException(
                    "error.booking.checkin.past"
            );
        }

        if (!checkOutDate.isAfter(checkInDate)) {
            throw new InvalidRequestException(
                    "error.booking.invalid.date.range"
            );
        }
    }

    private void validateCapacity(
            RoomType roomType,
            Integer adultCount,
            Integer childCount
    ) {
        if (adultCount == null || adultCount <= 0) {
            throw new InvalidRequestException(
                    "error.booking.adult.required"
            );
        }

        int normalizedChildCount =
                childCount == null ? 0 : childCount;

        if (normalizedChildCount < 0) {
            throw new InvalidRequestException(
                    "error.booking.child.count.negative"
            );
        }

        boolean capacityExceeded =
                adultCount > roomType.getMaxAdults()
                        || normalizedChildCount
                        > roomType.getMaxChildren()
                        || adultCount + normalizedChildCount
                        > roomType.getBaseCapacity();

        if (capacityExceeded) {
            throw new InvalidRequestException(
                    "error.booking.capacity.exceeded"
            );
        }
    }

    private void validateGuestInformation(
            ReservationCreateRequestDto request
    ) {
        if (request.guests() == null
                || request.guests().isEmpty()) {
            throw new InvalidRequestException(
                    "error.booking.guest.required"
            );
        }

        int childCount =
                request.childCount() == null
                        ? 0
                        : request.childCount();

        int expectedGuestCount =
                request.adultCount() + childCount;

        if (request.guests().size() != expectedGuestCount) {
            throw new InvalidRequestException(
                    "error.booking.guest.count.mismatch"
            );
        }

        long primaryGuestCount =
                request.guests()
                        .stream()
                        .filter(guest ->
                                Boolean.TRUE.equals(
                                        guest.primaryGuest()
                                )
                        )
                        .count();

        if (primaryGuestCount != 1) {
            throw new InvalidRequestException(
                    "error.booking.primary.guest.invalid"
            );
        }
    }

    private void validateRoomTypeStatus(RoomType roomType) {
        if (roomType.getStatus() != RoomTypeStatus.ACTIVE) {
            throw new ConflictException(
                    "error.booking.room.unavailable"
            );
        }
    }


    private Booking saveBooking(
            User user,
            BigDecimal totalPrice
    ) {
        Booking booking = new Booking();

        booking.setUser(user);
        booking.setBookingNumber(
                generateBookingNumber()
        );
        booking.setTotalAmount(totalPrice);
        booking.setStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);

        return bookingRepository.save(booking);
    }

    private String generateBookingNumber() {
        return "BK-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 10)
                        .toUpperCase();
    }

    private Reservation saveReservation(
            Booking booking,
            Hotel hotel,
            RoomType roomType,
            RoomPrice roomPrice,
            ReservationCreateRequestDto request,
            BigDecimal totalPrice
    ) {
        Reservation reservation = new Reservation();

        reservation.setBooking(booking);
        reservation.setHotel(hotel);
        reservation.setRoomType(roomType);
        reservation.setCheckInDate(request.checkInDate());
        reservation.setCheckOutDate(request.checkOutDate());
        reservation.setAdultCount(request.adultCount());
        reservation.setChildCount(
                request.childCount() == null
                        ? 0
                        : request.childCount()
        );
        reservation.setPricePerNight(
                roomPrice.getPricePerNight()
        );
        reservation.setTotalPrice(totalPrice);
        reservation.setStatus(
                ReservationStatus.PENDING
        );

        return reservationRepository.save(reservation);
    }

    private RoomPrice findRoomPriceForReservation(
            RoomType roomType,
            LocalDate checkInDate,
            LocalDate checkOutDate
    ) {
        return roomPriceRepository
                .findFirstByRoomTypeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        roomType.getId(),
                        checkInDate,
                        checkOutDate
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "error.room.price.not.found"
                        )
                );
    }

    private void saveReservationGuests(
            Reservation reservation,
            List<ReservationGuestCreateRequestDto> guestRequests
    ) {
        List<ReservationGuest> guests =
                guestRequests.stream()
                        .map(guestRequest -> {
                            ReservationGuest guest =
                                    new ReservationGuest();

                            guest.setReservation(reservation);
                            guest.setName(
                                    guestRequest.name()
                            );
                            guest.setSurname(
                                    guestRequest.surname()
                            );
                            guest.setGuestType(
                                    guestRequest.guestType()
                            );
                            guest.setPrimaryGuest(
                                    Boolean.TRUE.equals(
                                            guestRequest.primaryGuest()
                                    )
                            );

                            return guest;
                        })
                        .toList();

        reservationGuestRepository.saveAll(guests);
    }

    private void validateAllReservationsBelongToSameHotel(
            List<ReservationCreateRequestDto> reservations
    ) {
        long distinctHotelCount =
                reservations.stream()
                        .map(ReservationCreateRequestDto::hotelId)
                        .distinct()
                        .count();

        if (distinctHotelCount != 1) {
            throw new InvalidRequestException(
                    "error.booking.multiple.hotels.not.allowed"
            );
        }
    }

    private void validateReservationsNotEmpty(
            List<ReservationCreateRequestDto> reservations
    ) {
        if (reservations == null || reservations.isEmpty()) {
            throw new InvalidRequestException(
                    "error.booking.reservation.required"
            );
        }
    }

    private void validateAllReservationsHaveSameDates(
            List<ReservationCreateRequestDto> reservations
    ) {
        ReservationCreateRequestDto firstReservation =
                reservations.getFirst();

        boolean hasDifferentDates =
                reservations.stream()
                        .anyMatch(reservation ->
                                !firstReservation.checkInDate()
                                        .equals(reservation.checkInDate())
                                        || !firstReservation.checkOutDate()
                                        .equals(reservation.checkOutDate())
                        );

        if (hasDifferentDates) {
            throw new InvalidRequestException(
                    "error.booking.different.dates.not.allowed"
            );
        }
    }

    private BigDecimal calculateBookingTotal(
            List<PreparedReservation> preparedReservations
    ) {
        return preparedReservations.stream()
                .map(PreparedReservation::totalPrice)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    private PreparedReservation prepareReservation(
            Hotel hotel,
            ReservationCreateRequestDto request
    ) {
        RoomType roomType =
                findRoomType(request.roomTypeId());

        validateRoomTypeBelongsToHotel(
                roomType,
                hotel
        );

        validateRoomTypeStatus(roomType);

        validateDates(
                request.checkInDate(),
                request.checkOutDate()
        );

        validateCapacity(
                roomType,
                request.adultCount(),
                request.childCount()
        );

        validateGuestInformation(request);

        RoomPrice roomPrice =
                findRoomPriceForReservation(
                        roomType,
                        request.checkInDate(),
                        request.checkOutDate()
                );

        BigDecimal totalPrice =
                calculateTotalPrice(
                        roomPrice,
                        request.checkInDate(),
                        request.checkOutDate()
                );

        return new PreparedReservation(
                request,
                roomType,
                roomPrice,
                totalPrice
        );
    }

    private record PreparedReservation(
            ReservationCreateRequestDto request,
            RoomType roomType,
            RoomPrice roomPrice,
            BigDecimal totalPrice
    ) {
    }

    private record RoomAvailabilityKey(
            Long roomTypeId,
            LocalDate checkInDate,
            LocalDate checkOutDate
    ) {
    }
}

