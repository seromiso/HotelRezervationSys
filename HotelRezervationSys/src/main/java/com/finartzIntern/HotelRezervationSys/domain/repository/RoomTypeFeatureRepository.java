package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomTypeFeature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomTypeFeatureRepository extends JpaRepository<RoomTypeFeature,Long> {
    List<RoomTypeFeature> findByRoomTypeId(Long roomTypeId);
}
