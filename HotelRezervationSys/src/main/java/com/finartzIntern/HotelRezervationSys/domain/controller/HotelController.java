package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeSearchResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController {

    // DÜZELTME: 'private final' eklendi
    private final HotelService hotelService;

    // YENİ EKLENEN ENDPOINT
    @GetMapping("/{id}/room-types")
    public ResponseEntity<List<RoomTypeSearchResponseDto>> searchHotelRooms(
            @PathVariable("id") Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam Integer adults,
            @RequestParam Integer children) {

        List<RoomTypeSearchResponseDto> response = hotelService.searchAvailableRooms(
                id, checkIn, checkOut, adults, children
        );

        return ResponseEntity.ok(response);
    }
}