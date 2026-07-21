package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.HotelStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findByManagerId(Long managerId);
    List<Hotel> findByCityAndStatus(String city, HotelStatus status);
    List<Hotel> findByStatus(HotelStatus status);
}
