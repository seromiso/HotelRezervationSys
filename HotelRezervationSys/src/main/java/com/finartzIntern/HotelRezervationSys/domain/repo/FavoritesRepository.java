package com.finartzIntern.HotelRezervationSys.domain.repo;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    List<Favorites> findByUserId(Long userId);
    boolean existsByUserIdAndHotelId(Long userId, Long hotelId);
    void deleteByUserIdAndHotelId(Long userId, Long hotelId);
}
