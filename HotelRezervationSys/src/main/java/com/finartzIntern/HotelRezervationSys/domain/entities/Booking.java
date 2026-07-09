package com.finartzIntern.HotelRezervationSys.domain.entities;

import com.finartzIntern.HotelRezervationSys.domain.enums.BookingStatus;
import com.finartzIntern.HotelRezervationSys.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_number", nullable = false, unique = true)
    private String bookingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

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

    public void confirmBooking() {
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancelBooking() {
        this.status = BookingStatus.CANCELLED;
    }

    public void completeBooking() {
        this.status = BookingStatus.COMPLETED;
    }

    public void setPending() {
        this.status = BookingStatus.PENDING;
    }

    public void markPaymentAsPaid() {
        this.paymentStatus = PaymentStatus.PAID;
    }

    public void markPaymentAsFailed() {
        this.paymentStatus = PaymentStatus.FAILED;
    }

    public void markPaymentAsPending() {
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public void refundPayment() {
        this.paymentStatus = PaymentStatus.REFUNDED;
    }
}


