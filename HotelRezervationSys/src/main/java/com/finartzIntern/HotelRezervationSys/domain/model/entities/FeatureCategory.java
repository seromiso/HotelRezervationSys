package com.finartzIntern.HotelRezervationSys.domain.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feature_categories")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeatureCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Örn: "Tesis Özellikleri", "Oda Olanakları"

    @Column(nullable = false, length = 20)
    private String type; // Şemadaki HOTEL veya ROOM ayrımı için
}