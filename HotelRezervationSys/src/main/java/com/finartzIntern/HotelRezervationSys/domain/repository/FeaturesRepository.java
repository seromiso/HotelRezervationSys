package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.Features;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeaturesRepository extends JpaRepository<Features,Long> {

    List<Features> findAllFeatures(Long id);

}
