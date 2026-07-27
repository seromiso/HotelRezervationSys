package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelImageCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/owner/hotels")
@RequiredArgsConstructor
public class HotelImageController {

    private final HotelImageService hotelImageService;

    @PostMapping("/{id}/images")
    public ResponseEntity<String> addImagesToHotel(
            @PathVariable("id") Long hotelId,
            @RequestBody HotelImageCreateRequestDto requestDto) {

        // DİKKAT: Normalde projelerde giriş yapmış kullanıcının ID'sini
        // Spring Security Context'ten (JWT Token içinden) alırız.
        // Şimdilik Security katmanın tam olmadığı için test edebilmen adına
        // bunu manuel olarak "1" (veya veritabanındaki geçerli bir manager_id) veriyorum.
        Long currentUserId = 1L;

        hotelImageService.addImagesToHotel(hotelId, requestDto, currentUserId);

        return ResponseEntity.status(HttpStatus.CREATED).body("Görseller başarıyla eklendi.");
    }

    @DeleteMapping("/{id}/images/{imageId}")
    public ResponseEntity<String> deleteImageFromHotel(
            @PathVariable("id") Long hotelId,
            @PathVariable("imageId") Long imageId) {

        // Henüz Security Context (JWT) entegrasyonu olmadığı için
        // test edebilmen adına otel sahibinin ID'sini manuel (1L) veriyoruz.
        Long currentUserId = 1L;

        hotelImageService.deleteImageFromHotel(hotelId, imageId, currentUserId);

        return ResponseEntity.ok("Görsel başarıyla silindi.");
    }
}