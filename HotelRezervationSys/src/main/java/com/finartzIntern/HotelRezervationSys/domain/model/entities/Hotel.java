package com.finartzIntern.HotelRezervationSys.domain.model.entities;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.HotelStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Set;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "manager_id",nullable = false)
    private Long managerId;

    @Column(nullable = false,length = 100)
    private String name;

    @Column(nullable = false,length = 50)
    private String city;

    @Column(nullable = false,length = 50)
    private String district;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "commission_rate",nullable = false ,precision = 4,scale = 2)
    private BigDecimal commissionRate;

    @Column(name = "iban_no", nullable = false,length = 34)
    private String ibanNo;

    @Column(name = "check_in_time",length = 5)
    private String checkInTime;

    @Column(name = "check_out_time",length = 5)
    private String checkOutTime;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HotelStatus status;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HotelImage> images;

    // 2. Oda Tipleri İlişkisi
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoomType> roomTypes;


    // hotel_features ara tablosunu Hibernate'e otomatik yönettiriyoruz
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "hotel_features",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private Set<Features> features;


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


    public void archiveHotel(){
        this.status = HotelStatus.INACTIVE;
    }

    public void activateHotel(){
        this.status = HotelStatus.ACTIVE;
    }

    public void addFeature(Features feature){
        if(this.features == null){
            features = new java.util.HashSet<>();
        }
        this.features.add(feature);
    }

    public void deleteFeature(Features feature){
        if(this.features != null){
            features.remove(feature);
        }
    }

}














