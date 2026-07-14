package com.finartzIntern.HotelRezervationSys.domain.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "image_categories")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Örn: "ROOM", "LOBBY", "POOL", "RESTAURANT"

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}