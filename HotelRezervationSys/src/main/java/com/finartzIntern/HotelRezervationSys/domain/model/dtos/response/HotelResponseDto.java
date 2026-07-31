package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;



import com.finartzIntern.HotelRezervationSys.domain.model.enums.HotelStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record HotelResponseDto (
     Long id,
     Long managerId,
     String name,
     String city,
     String district,
     String address,
     String phone,
     String description,
     BigDecimal commissionRate,
     String ibanNo,
     HotelStatus status,
     String checkInTime,
     String checkOutTime,
     LocalDateTime updatedAt,
     LocalDateTime createdAt
     )
{}