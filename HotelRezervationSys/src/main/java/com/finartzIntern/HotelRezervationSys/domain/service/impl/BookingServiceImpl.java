package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ConflictException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.InvalidRequestException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.BookingCreateRequestDto;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

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
        return bookingRepository.findAllByUser_Id(userId)
                .stream()
                .map(this::toBookingResponse)
                .toList();

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

    @Override
    @Transactional
    public BookingResponseDto createBooking(
            BookingCreateRequestDto request
    ) {
        // 1. Giriş yapan kullanıcıyı bul
        // 2. Hotel ve RoomType entity'lerini yükle
        // 3. RoomType-hotel ilişkisini doğrula
        // 4. Tarihleri doğrula
        // 5. Kişi sayısı ve guest listesini doğrula
        // 6. Kapasiteyi doğrula
        // 7. Müsaitliği yeniden kontrol et
        // 8. Güncel fiyatı hesapla
        // 9. Booking kaydet
        // 10. Reservation kaydet
        // 11. ReservationGuest kayıtlarını kaydet
        // 12. Response döndür

        User currentUser = findUser(request.userId());

        Hotel hotel = findHotel(request.reservation().hotelId());

        RoomType roomType = findRoomType(request.reservation().roomTypeId());

        validateRoomTypeBelongsToHotel(roomType, hotel);
        validateRoomTypeStatus(roomType);
        validateDates(request);
        validateGuestInformation(request);
        validateCapacity(roomType, request);

        validateAvailability(roomType, request);

        RoomPrice roomPrice =
                findRoomPriceForReservation(
                        roomType,
                        request
                );

        BigDecimal totalPrice =
                calculateTotalPrice(
                        roomPrice,
                        request
                );

        Booking savedBooking =
                saveBooking(
                        currentUser,
                        totalPrice
                );

        Reservation savedReservation =
                saveReservation(
                        savedBooking,
                        hotel,
                        roomType,
                        roomPrice,
                        request,
                        totalPrice
                );

        saveReservationGuests(
                savedReservation,
                request
        );

        return toBookingResponse(savedBooking);
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

                            return toReservationDetailResponse(
                                    reservation,
                                    guests
                            );
                        })
                        .toList();

        return new BookingDetailResponseDto(
                booking.getId(),
                booking.getBookingNumber(),
                booking.getTotalAmount(),
                booking.getStatus(),
                booking.getPaymentStatus(),
                booking.getCreatedAt(),
                reservationResponses
        );
    }

    private ReservationDetailResponseDto toReservationDetailResponse(
            Reservation reservation,
            List<ReservationGuest> guests
    ) {
        List<ReservationGuestResponseDto> guestResponses =
                guests.stream()
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

    private ReservationGuestResponseDto toReservationGuestResponse(
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

    private BookingHotelResponseDto toHotelResponse(
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

    private RoomTypeResponseDto toRoomTypeResponse(
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

    private BigDecimal calculateTotalPrice(
            RoomPrice roomPrice,
            BookingCreateRequestDto request
    ) {
        LocalDate checkInDate =
                request.reservation().checkInDate();

        LocalDate checkOutDate =
                request.reservation().checkOutDate();

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

    private void validateDates(
            BookingCreateRequestDto request
    ) {
        LocalDate checkInDate =
                request.reservation().checkInDate();

        LocalDate checkOutDate =
                request.reservation().checkOutDate();

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
            BookingCreateRequestDto request
    ) {
        Integer adultCount = request.reservation().adultCount();
        Integer childCount = request.reservation().childCount();

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
                        || normalizedChildCount > roomType.getMaxChildren()
                        || adultCount + normalizedChildCount
                        > roomType.getBaseCapacity();

        if (capacityExceeded) {
            throw new InvalidRequestException(
                    "error.booking.capacity.exceeded"
            );
        }
    }

    private void validateGuestInformation(
            BookingCreateRequestDto request
    ) {
        if (request.guests() == null
                || request.guests().isEmpty()) {
            throw new InvalidRequestException(
                    "error.booking.guest.required"
            );
        }

        int adultCount =
                request.reservation().adultCount();

        int childCount =
                request.reservation().childCount() == null
                        ? 0
                        : request.reservation().childCount();

        int expectedGuestCount =
                adultCount + childCount;

        if (request.guests().size() != expectedGuestCount) {
            throw new InvalidRequestException(
                    "error.booking.guest.count.mismatch"
            );
        }

        long primaryGuestCount = request.guests()
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
            throw new IllegalArgumentException(
                    "error.booking.room.unavailable"
            );
        }
    }

    private void validateAvailability(
            RoomType roomType,
            BookingCreateRequestDto request
    ) {
        long reservedRoomCount =
                reservationRepository.countOverlappingReservations(
                        roomType.getId(),
                        request.reservation().checkInDate(),
                        request.reservation().checkOutDate(),
                        ReservationStatus.CANCELLED
                );

        if (reservedRoomCount >= roomType.getTotalInventory()) {

            throw new ConflictException(
                    "error.booking.no.availability"
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
            BookingCreateRequestDto request,
            BigDecimal totalPrice
    ){
        Reservation reservation = new Reservation();

        reservation.setBooking(booking);
        reservation.setHotel(hotel);
        reservation.setRoomType(roomType);

        reservation.setCheckInDate(
                request.reservation().checkInDate()
        );

        reservation.setCheckOutDate(
                request.reservation().checkOutDate()
        );

        reservation.setAdultCount(
                request.reservation().adultCount()
        );

        reservation.setChildCount(
                request.reservation().childCount() == null
                        ? 0
                        : request.reservation().childCount()
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
            BookingCreateRequestDto request
    ) {
        LocalDate checkInDate =
                request.reservation().checkInDate();

        LocalDate checkOutDate =
                request.reservation().checkOutDate();

        return roomPriceRepository
                .findFirstByRoomTypeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        roomType.getId(),
                        checkInDate,
                        checkOutDate
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No price information found for selected dates."
                        )
                );
    }

    private void saveReservationGuests(
            Reservation reservation,
            BookingCreateRequestDto request
    ) {
        List<ReservationGuest> guests =
                request.guests()
                        .stream()
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
}
