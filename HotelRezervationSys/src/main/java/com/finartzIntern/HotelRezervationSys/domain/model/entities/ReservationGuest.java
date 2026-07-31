package com.finartzIntern.HotelRezervationSys.domain.model.entities;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.GuestType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "reservation_guests")
@NoArgsConstructor
@AllArgsConstructor
public class ReservationGuest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(name = "guest_type", nullable = false)
    private GuestType guestType;

    @Column(name = "is_primary_guest", nullable = false)
    private Boolean primaryGuest = false;   // güvenli varsayılan

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // basit, zararsız yardımcı metodlar — okunabilirlik için kalabilir
    public void makePrimaryGuest() {
        this.primaryGuest = true;
    }

    public void removePrimaryGuest() {
        this.primaryGuest = false;
    }
}