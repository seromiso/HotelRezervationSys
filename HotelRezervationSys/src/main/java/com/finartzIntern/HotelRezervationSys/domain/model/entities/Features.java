package com.finartzIntern.HotelRezervationSys.domain.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "features")
@Getter  @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Features {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Her özellik kesinlikle bir kategoriye bağlı olmalıdır
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private FeatureCategory category;

    @Column(nullable = false)
    private String name; // Örn: "Ücretsiz Wi-Fi", "Her Şey Dahil", "Klima"

    @Column(nullable = false, length = 20)
    private String type; // Şemadaki HOTEL veya ROOM ayrımı için
}
