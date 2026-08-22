package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


public record HotelSearchCriteria(
        @NotBlank(message = "Şehir alanı boş bırakılamaz")
        String city,
        String district,
        @NotNull(message = "Giriş tarihi zorunludur")
        @FutureOrPresent(message = "Giriş tarihi bugün veya gelecek bir tarih olmalıdır")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate checkInDate,

        @NotNull(message = "Çıkış tarihi zorunludur")
        @Future(message = "Çıkış tarihi gelecekte bir tarih olmalıdır")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate checkOutDate,

        @NotNull(message = "Kişi sayısı zorunludur")
        @Min(value = 1, message = "Kişi sayısı en az 1 olmalıdır")
        Integer guestCount
) {}


