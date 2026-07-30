package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    // Şehir ID'sine göre ilçeleri getiren metot
    List<District> findByCityIdOrderByNameAsc(Long cityId);

    // Güvenlik doğrulaması için: Bu ilçe gerçekten bu şehre mi ait?
    boolean existsByIdAndCityId(Long districtId, Long cityId);
}