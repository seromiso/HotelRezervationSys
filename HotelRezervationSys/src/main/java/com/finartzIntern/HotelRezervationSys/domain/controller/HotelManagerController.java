package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelRegistrationRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelUpdateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
    @RequestMapping("/api/v1/hotel")
    @RequiredArgsConstructor
    public class HotelManagerController {

        private final HotelManagerService hotelManagerService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HotelResponseDto> registerHotel(
            @Valid @ModelAttribute HotelRegistrationRequestDto requestDto) {

        HotelResponseDto response = hotelManagerService.registerHotel(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/")
    public ResponseEntity<HotelResponseDto> updateHotelInfo(
            @RequestParam("hotelId") Long hotelId,
            @Valid @RequestBody HotelUpdateRequestDto dto) {

        HotelResponseDto updatedHotel = hotelManagerService.updateHotelInfo(hotelId, dto);
        return ResponseEntity.ok(updatedHotel);
    }

    }

