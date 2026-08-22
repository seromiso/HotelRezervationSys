package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.ReservationGuest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationGuestRepository extends JpaRepository<ReservationGuest, Long> {
    List<ReservationGuest> findAllByReservation_Id(Long reservationId);
}
