package com.finartzIntern.HotelRezervationSys.domain.model.entities;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserRole;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Enumerated(EnumType.STRING) // Veritabanına "ADMIN", "MANAGER" şeklinde metin olarak yazar
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(name = "datebirth", nullable = false)
    private LocalDate dateBirth; // Şemadaki datetime karşılığı

    @Column(name = "phonenumber", length = 20) //
    private String phoneNumber;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private UserStatus status; // ACTIVE, INACTIVE



    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
