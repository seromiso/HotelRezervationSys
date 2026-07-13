package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomType;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.RoomTypeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    List<RoomType> findByHotelId(Long hotelId); // Otele ait tüm oda tiplerini listeler
    List<RoomType> findByHotelIdAndStatus(Long hotelId, RoomTypeStatus status); // Otele ait aktif olan oda tiplerini listeler (Müşteri için)
}
