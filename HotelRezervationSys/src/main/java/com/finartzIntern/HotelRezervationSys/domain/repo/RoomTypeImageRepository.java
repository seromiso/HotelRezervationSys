package com.finartzIntern.HotelRezervationSys.domain.repo;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomTypeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeImageRepository extends JpaRepository<RoomTypeImage, Long>  {
    List<RoomTypeImage> findByRoomTypeIdOrderByDisplayOrderAsc(Long roomTypeId); // Oda tipine ait resimleri display ordera göre küçükten büyüğe getirir
}
