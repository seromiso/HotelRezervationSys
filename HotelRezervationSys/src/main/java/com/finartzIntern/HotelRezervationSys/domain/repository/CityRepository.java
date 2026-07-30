package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    // 1. Şehir adına göre arama yapmak için (Büyük/küçük harf duyarsız)
    Optional<City> findByNameIgnoreCase(String name);

    // 2. Alternatif: Direkt eşitlik ile isim arama (HotelManagerServiceImpl içinde kullandığımız metot)
    Optional<City> findByName(String name);

    // 3. A-Z alfabetik sırayla tüm şehirleri getirmek için (Frontend scroll/dropdown listesi için)
    List<City> findAllByOrderByNameAsc();

    // 4. Şehrin varlığını kontrol etmek için
    boolean existsByNameIgnoreCase(String name);
}