package com.finartzIntern.HotelRezervationSys.domain.repository;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomTypeFeature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomTypeFeatureRepository extends JpaRepository<RoomTypeFeature,Long> {

    List<RoomTypeFeature> findByRoomType_Id(Long roomTypeId);
    Optional<RoomTypeFeature> findByRoomType_IdAndFeature_Id(Long roomTypeId, Long featureId);
    boolean existsByRoomType_IdAndFeature_Id(Long roomTypeId, Long featureId);
    void deleteByRoomType_IdAndFeature_Id(Long roomTypeId, Long featureId);
}
