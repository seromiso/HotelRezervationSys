package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentCreateRequestDto {

    private BigDecimal amount;
    private String currency;
    private String paymentMethod;

}