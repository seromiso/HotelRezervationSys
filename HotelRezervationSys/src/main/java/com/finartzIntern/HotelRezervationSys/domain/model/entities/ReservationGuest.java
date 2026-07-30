package com.finartzIntern.HotelRezervationSys.domain.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "guest_type", nullable = false)
    private String guestType;

    @Column(name = "is_primary_guest", nullable = false)
    private Boolean primaryGuest;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();

    }

    public void makePrimaryGuest() {
        this.primaryGuest = true;
    }

    public void removePrimaryGuest() {
        this.primaryGuest = false;
    }
}