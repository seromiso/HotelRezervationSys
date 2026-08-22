package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.Booking;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Features;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeaturesRepository extends JpaRepository<Features,Long> {
    Optional<Features> findById(Long id);
    List<Features> findByType(String type);
}
