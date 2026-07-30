package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.HotelStatus;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findByManagerId(Long managerId);
    List<Hotel> findByCityAndStatus(String city, HotelStatus status);
    List<Hotel> findByStatus(HotelStatus status);

    @Query("SELECT DISTINCT h FROM Hotel h JOIN h.roomTypes rt " +
            "WHERE LOWER(h.city) = LOWER(:city) " +
            "AND (:district IS NULL OR LOWER(h.district) = LOWER(:district)) " +
            "AND h.status = :hotelStatus " +
            "AND rt.baseCapacity >= :guestCount " +
            "AND rt.totalInventory > (" +
            "    SELECT COUNT(res) FROM Reservation res " +
            "    WHERE res.roomType.id = rt.id " +
            "    AND res.status <> :cancelledStatus " +
            "    AND res.checkInDate < :checkOutDate " +
            "    AND res.checkOutDate > :checkInDate" +
            ")")
    List<Hotel> searchHotels(
            @Param("city") String city,
            @Param("district") String district,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("guestCount") Integer guestCount,
            @Param("hotelStatus") HotelStatus hotelStatus,
            @Param("cancelledStatus") ReservationStatus cancelledStatus
    );
}

