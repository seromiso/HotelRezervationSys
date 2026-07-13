package com.finartzIntern.HotelRezervationSys.domain.repo;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelImageRepository extends JpaRepository<HotelImage,Long> {

    List<HotelImage> findAllHotelImage(Long id);
}
