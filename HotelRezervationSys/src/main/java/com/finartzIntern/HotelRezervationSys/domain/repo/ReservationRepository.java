package com.finartzIntern.HotelRezervationSys.domain.repo;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
