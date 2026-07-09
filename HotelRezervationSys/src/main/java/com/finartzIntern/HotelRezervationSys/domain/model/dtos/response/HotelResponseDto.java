package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HotelResponseDto {
    private Long id;
    private Long managerId;
    private String name;
    private String city;
    private String district;
    private String address;
    private String phone;
    private String description;
    private BigDecimal commissionRate;
    private String ibanNo;
    private String status;
    private String checkInTime;
    private String checkOutTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}