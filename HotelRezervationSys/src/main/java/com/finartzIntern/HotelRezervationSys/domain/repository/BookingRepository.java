package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.Booking;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.BookingStatus;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByUser_Id(Long userId);
    Optional<Booking> findByBookingNumber(String bookingNumber);

    @Query(
            value = """
                SELECT DISTINCT b
                FROM Booking b
                JOIN b.reservations r
                WHERE r.hotel.id = :hotelId
                  AND (:bookingStatus IS NULL OR b.status = :bookingStatus)
                  AND (:reservationStatus IS NULL OR r.status = :reservationStatus)
                  AND r.checkInDate >= COALESCE(:checkInFrom, r.checkInDate)
                  AND r.checkInDate <= COALESCE(:checkInTo, r.checkInDate)
                """,
            countQuery = """
                SELECT COUNT(DISTINCT b.id)
                FROM Booking b
                JOIN b.reservations r
                WHERE r.hotel.id = :hotelId
                  AND (:bookingStatus IS NULL OR b.status = :bookingStatus)
                  AND (:reservationStatus IS NULL OR r.status = :reservationStatus)
                  AND r.checkInDate >= COALESCE(:checkInFrom, r.checkInDate)
                  AND r.checkInDate <= COALESCE(:checkInTo, r.checkInDate)
                """
    )
    Page<Booking> findOwnerHotelBookings(
            @Param("hotelId") Long hotelId,
            @Param("bookingStatus") BookingStatus bookingStatus,
            @Param("reservationStatus") ReservationStatus reservationStatus,
            @Param("checkInFrom") LocalDate checkInFrom,
            @Param("checkInTo") LocalDate checkInTo,
            Pageable pageable
    );

    @Query("""
        SELECT DISTINCT b
        FROM Booking b
        JOIN b.reservations r
        WHERE b.bookingNumber = :bookingNumber
          AND r.hotel.id = :hotelId
        """)
    Optional<Booking> findOwnerBookingDetail(
            @Param("hotelId") Long hotelId,
            @Param("bookingNumber") String bookingNumber
    );

}
