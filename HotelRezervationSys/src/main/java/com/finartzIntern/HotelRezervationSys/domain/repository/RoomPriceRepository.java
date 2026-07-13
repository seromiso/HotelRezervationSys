package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface RoomPriceRepository extends JpaRepository<RoomPrice, Long> {
    List<RoomPrice> findByRoomTypeId(Long roomTypeId); // Oda tipine ait fiyatları listeler
    List<RoomPrice> findByRoomTypeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long roomTypeId, LocalDate endDate, LocalDate startDate);// Rezervasyon yapılacak tarihlerde fiyatları hesaplar
}
