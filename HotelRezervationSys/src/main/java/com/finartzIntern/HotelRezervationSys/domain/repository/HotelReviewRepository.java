package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.HotelReviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HotelReviewRepository extends JpaRepository<HotelReviews,Long> {
    List<HotelReviews> findByHotel_IdOrderByCreatedAtDesc(Long hotelId);
    Optional<HotelReviews> findByReservationId(Long reservationId);
    Boolean existsByReservationId(Long reservationId);
    @Query("select avg(r.rating) from HotelReviews r where r.hotel.id = :hotelId")
    Double findAverageRatingByHotelId(Long hotelId);
}
