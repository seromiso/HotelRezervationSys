package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.PaymentCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.PaymentResponseDto;

import java.util.List;

public interface PaymentService {

    PaymentResponseDto getPaymentById(Long id);

    PaymentResponseDto createPayment(PaymentCreateRequestDto paymentRequestDto);

    List<PaymentResponseDto> getAllPayments();

}