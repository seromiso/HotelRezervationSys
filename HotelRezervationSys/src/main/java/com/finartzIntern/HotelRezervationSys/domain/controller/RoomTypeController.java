package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeDetailResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeImageResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeSearchResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.RoomTypeImageService;
import com.finartzIntern.HotelRezervationSys.domain.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1") // Müşteri tarafı olduğu için owner YOK
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;
    private final RoomTypeImageService roomTypeImageService;

    // Belgedeki 1. Fotoğraf: Müşteri Arama Motoru
    @GetMapping("/hotels/{hotelId}/room-types")
    public ResponseEntity<List<RoomTypeSearchResponseDto>> searchRoomTypes(
            @PathVariable Long hotelId,
            @RequestParam("checkIn") LocalDate checkIn,
            @RequestParam("checkOut") LocalDate checkOut,
            @RequestParam("adults") Integer adults,
            @RequestParam(value = "children", defaultValue = "0") Integer children) {

        return ResponseEntity.ok(roomTypeService.searchRoomTypes(hotelId, checkIn, checkOut, adults, children));
    }

    // Belgedeki 1. Fotoğraf: Müşteri Oda Detayı
    @GetMapping("/room-types/{id}")
    public ResponseEntity<RoomTypeDetailResponseDto> getRoomTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(roomTypeService.getRoomTypeById(id));
    }

    // Belgedeki 1. Fotoğraf: Müşteri resim listeleme
    @GetMapping("/room-types/{id}/images")
    public ResponseEntity<List<RoomTypeImageResponseDto>> getImagesByRoomTypeId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roomTypeImageService.getImagesByRoomTypeId(id));
    }
}