package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDto {
    private Long id;
    private Long bookingId;
    private BigDecimal amount;
    private String paymentMethod;
    private PaymentStatus status;
    private String currency;
    private String transactionId;
    private LocalDateTime createdAt;

}