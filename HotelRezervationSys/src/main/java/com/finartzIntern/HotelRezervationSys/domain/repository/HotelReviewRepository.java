package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.HotelReviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HotelReviewRepository extends JpaRepository<HotelReviews,Long> {
    List<HotelReviews> findByHotel_IdOrderByCreatedAtDesc(Long hotelId);
    Page<HotelReviews> findByHotel_IdOrderByCreatedAtDesc(Long hotelId, Pageable pageable);
    Optional<HotelReviews> findByReservationId(Long reservationId);
    Boolean existsByReservationId(Long reservationId);
    Long countByHotel_Id(Long hotelId);
    @Query("select avg(r.rating) from HotelReviews r where r.hotel.id = :hotelId")
    Double findAverageRatingByHotelId(Long hotelId);
    @Query("""
            select r.rating, count(r)
            from HotelReviews r
            where r.hotel.id = :hotelId
            group by r.rating
            """)
    List<Object[]> countReviewsByRatingForHotel(Long hotelId);
}
