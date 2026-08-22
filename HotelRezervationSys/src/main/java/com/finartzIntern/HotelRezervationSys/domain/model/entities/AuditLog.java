package com.finartzIntern.HotelRezervationSys.domain.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter  @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // İşlemi yapan kullanıcı (Giriş yapmamış bir ziyaretçi ise null kalabilir, o yüzden nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "action")
    private String action; // Örn: "USER_LOGIN", "HOTEL_UPDATE", "BOOKING_CANCEL"

    @Column(name = "entity_name")
    private String entityName; // Örn: "Hotel", "Booking"

    @Column(name = "entity_id")
    private Long entityId; // Değişen kaydın ID'si

    @Column(name = "endpoint")
    private String endpoint; // Örn: "/api/v1/hotels/5"

    @Column(name = "ip_address")
    private String ipAddress; // İşlemi yapanın IP adresi

    @Column(name = "status_code")
    private Integer statusCode; // Örn: 200, 403, 500

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}