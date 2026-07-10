package com.finartzIntern.HotelRezervationSys.domain.repo;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.ReservationGuest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationGuestRepository extends JpaRepository<ReservationGuest, Long> {

}
