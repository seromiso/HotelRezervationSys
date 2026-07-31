package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record HotelRegistrationRequestDto(
        @NotBlank(message = "Otel ismi boş olamaz !")
        String name,

        @NotNull(message = "Lütfen şehir seçiniz !")
        Long cityId,

        @NotNull(message = "Lütfen ilçe seçiniz !")
        Long districtId,

        @NotBlank(message = " Adres bilgilerini giriniz.")
        String address,

        @NotBlank(message = " Telefon numarası giriniz.")
        String phone,

        String description,

        @NotBlank(message = "IBAN numarası zorunludur")
        String ibanNo,

        @NotNull(message = "Ruhsat belgesi yüklenmelidir")
        MultipartFile licenseDocument,

        String checkInTime,

        String checkOutTime



) {
}
