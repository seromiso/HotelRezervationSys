package com.finartzIntern.HotelRezervationSys.domain.repo;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.HotelReviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HotelReviewRepository extends JpaRepository<HotelReviews,Long> {
    List<HotelReviews> findByHotelId(Long hotelId);
    Optional<HotelReviews> findByReservationId(Long reservationId);
}
