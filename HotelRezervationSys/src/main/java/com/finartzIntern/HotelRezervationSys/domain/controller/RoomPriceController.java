package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RoomPriceCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomPriceResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.RoomPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/owner/room-types")
@RequiredArgsConstructor
public class RoomPriceController {

    private final RoomPriceService roomPriceService;

    // POST /api/v1/owner/room-types/{id}/prices
    @PostMapping("/{id}/prices")
    public ResponseEntity<RoomPriceResponseDto> addRoomPrice(
            @PathVariable("id") Long id,
            @RequestBody RoomPriceCreateRequestDto requestDto) {

        RoomPriceResponseDto response = roomPriceService.addRoomPrice(id, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/v1/owner/room-types/{id}/prices
    @GetMapping("/{id}/prices")
    public ResponseEntity<List<RoomPriceResponseDto>> getRoomPricesByRoomTypeId(
            @PathVariable("id") Long id) {

        List<RoomPriceResponseDto> responseList = roomPriceService.getRoomPricesByRoomTypeId(id);
        return ResponseEntity.ok(responseList);
    }

    // PATCH /api/v1/owner/room-types/{id}/prices/{priceId}
    @PatchMapping("/{id}/prices/{priceId}")
    public ResponseEntity<RoomPriceResponseDto> updateRoomPrice(
            @PathVariable("id") Long id,
            @PathVariable("priceId") Long priceId,
            @RequestBody RoomPriceCreateRequestDto requestDto) {

        RoomPriceResponseDto response = roomPriceService.updateRoomPrice(priceId, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/prices/{priceId}")
    public ResponseEntity<Void> deleteRoomPrice(@PathVariable Long priceId) {
        roomPriceService.deleteRoomPrice(priceId);
        return ResponseEntity.noContent().build();
    }
}