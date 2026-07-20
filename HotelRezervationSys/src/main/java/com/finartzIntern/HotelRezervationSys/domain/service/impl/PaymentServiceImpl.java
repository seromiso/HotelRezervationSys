package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.PaymentCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.PaymentResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Booking;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Payment;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.PaymentStatus;
import com.finartzIntern.HotelRezervationSys.domain.repository.BookingRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.PaymentRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentById(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ödeme bulunamadı! ID: " + id));

        PaymentResponseDto response = new PaymentResponseDto();

        response.setId(payment.getId());
        response.setBookingId(payment.getBooking().getId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus());
        response.setCurrency(payment.getCurrency());
        response.setTransactionId(payment.getTransactionId());
        response.setCreatedAt(payment.getCreatedAt());

        return response;
    }

    @Override
    @Transactional
    public PaymentResponseDto createPayment(PaymentCreateRequestDto paymentRequestDto) {

        Booking booking = bookingRepository.findById(paymentRequestDto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Rezervasyon bulunamadı!"));

        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setAmount(paymentRequestDto.getAmount());
        payment.setCurrency(paymentRequestDto.getCurrency());
        payment.setPaymentMethod(paymentRequestDto.getPaymentMethod());

        // Kullanıcı göndermez, sistem belirler
        payment.setStatus(PaymentStatus.PENDING);

        Payment savedPayment = paymentRepository.save(payment);

        PaymentResponseDto response = new PaymentResponseDto();

        response.setId(savedPayment.getId());
        response.setBookingId(savedPayment.getBooking().getId());
        response.setAmount(savedPayment.getAmount());
        response.setPaymentMethod(savedPayment.getPaymentMethod());
        response.setStatus(savedPayment.getStatus());
        response.setCurrency(savedPayment.getCurrency());
        response.setTransactionId(savedPayment.getTransactionId());
        response.setCreatedAt(savedPayment.getCreatedAt());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getAllPayments() {

        List<Payment> payments = paymentRepository.findAll();

        return payments.stream().map(payment -> {

            PaymentResponseDto response = new PaymentResponseDto();

            response.setId(payment.getId());
            response.setBookingId(payment.getBooking().getId());
            response.setAmount(payment.getAmount());
            response.setPaymentMethod(payment.getPaymentMethod());
            response.setStatus(payment.getStatus());
            response.setCurrency(payment.getCurrency());
            response.setTransactionId(payment.getTransactionId());
            response.setCreatedAt(payment.getCreatedAt());

            return response;

        }).collect(Collectors.toList());
    }
}
