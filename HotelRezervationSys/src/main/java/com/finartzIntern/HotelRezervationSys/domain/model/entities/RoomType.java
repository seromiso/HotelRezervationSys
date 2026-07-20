package com.finartzIntern.HotelRezervationSys.domain.model.entities;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.RoomTypeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "room_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(name = "max_adults", nullable = false)
    private Integer maxAdults;

    @Column(name = "max_children", nullable = false)
    private Integer maxChildren;

    @Column(name = "base_capacity", nullable = false)
    private Integer baseCapacity;

    @Column(name = "bed_configuration", nullable = false)
    private String bedConfiguration;

    @Column(name = "total_inventory", nullable = false)
    private Integer totalInventory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomTypeStatus status;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomTypeImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomPrice> prices = new ArrayList<>();

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "room_type_features",
            joinColumns = @JoinColumn(name = "room_type_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private Set<Features> features = new HashSet<>();

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


    public void addFeature(Features feature) {
        if (this.features == null) {
            this.features = new HashSet<>();
        }
        this.features.add(feature);
    }

    public void removeFeature(Features feature) {
        if (this.features != null) {
            this.features.remove(feature);
        }
    }


    public void addImage(RoomTypeImage image) {
        this.images.add(image);
        image.setRoomType(this);
    }

    public void removeImage(RoomTypeImage image) {
        this.images.remove(image);
        image.setRoomType(null);
    }


    public void addPrice(RoomPrice price) {
        this.prices.add(price);
        price.setRoomType(this);
    }

    public void removePrice(RoomPrice price) {
        this.prices.remove(price);
        price.setRoomType(null);
    }


    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setRoomType(this);
    }

    public void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.setRoomType(null);
    }


    public void activateRoomType() {
        this.status = RoomTypeStatus.ACTIVE;
    }

    public void deactivateRoomType() {
        this.status = RoomTypeStatus.INACTIVE;
    }
}