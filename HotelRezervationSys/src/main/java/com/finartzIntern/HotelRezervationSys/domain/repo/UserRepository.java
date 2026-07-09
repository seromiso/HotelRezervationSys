package com.finartzIntern.HotelRezervationSys.domain.repo;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Giriş (Login) işlemlerinde e-posta kontrolü için
    boolean existsByEmail(String email);     // Yeni kayıt (Register) olurken e-posta çakışması kontrolü için
}
