package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.Reservation;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByBooking_Id(Long bookingId);

    @Query("""
            SELECT COUNT(r)
            FROM Reservation r
            WHERE r.roomType.id = :roomTypeId
              AND r.status <> :cancelledStatus
              AND r.checkInDate < :checkOutDate
              AND r.checkOutDate > :checkInDate
            """)
    long countOverlappingReservations(
            @Param("roomTypeId") Long roomTypeId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("cancelledStatus") ReservationStatus cancelledStatus
    );

    @Query("""
            SELECT COUNT(r)
            FROM Reservation r
            WHERE r.roomType.id = :roomTypeId
              AND r.status <> :cancelledStatus
              AND r.checkOutDate >= CURRENT_DATE
            """)
    int countActiveReservationsByRoomTypeId(
            @Param("roomTypeId") Long roomTypeId,
            @Param("cancelledStatus") ReservationStatus cancelledStatus
    );
}
