package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.PaymentCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.PaymentResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/bookings")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable Long id) {
        PaymentResponseDto response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<PaymentResponseDto> createPayment(
            @PathVariable Long bookingId,
            @RequestBody PaymentCreateRequestDto paymentRequestDto) {

        PaymentResponseDto response =
                paymentService.createPayment(bookingId, paymentRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {
        List<PaymentResponseDto> response = paymentService.getAllPayments();
        return ResponseEntity.ok(response);
    }


}