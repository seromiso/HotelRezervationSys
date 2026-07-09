package com.finartzIntern.HotelRezervationSys.domain.repo;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findByCityIgnoreCase(String city); // Şehre göre otel arama filtrelerinde kullanmak için
    List<Hotel> findByManagerId(Long managerId);
}
