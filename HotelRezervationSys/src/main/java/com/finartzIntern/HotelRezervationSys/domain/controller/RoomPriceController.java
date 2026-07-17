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
@RequestMapping("/api/v1/room-prices")
@RequiredArgsConstructor
public class RoomPriceController {

    private final RoomPriceService roomPriceService;

    @PostMapping("/room-type/{roomTypeId}")
    public ResponseEntity<RoomPriceResponseDto> addRoomPrice(
            @PathVariable Long roomTypeId,
            @RequestBody RoomPriceCreateRequestDto requestDto) {

        RoomPriceResponseDto response = roomPriceService.addRoomPrice(roomTypeId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/room-type/{roomTypeId}")
    public ResponseEntity<List<RoomPriceResponseDto>> getRoomPricesByRoomTypeId(
            @PathVariable Long roomTypeId) {

        List<RoomPriceResponseDto> responseList = roomPriceService.getRoomPricesByRoomTypeId(roomTypeId);
        return ResponseEntity.ok(responseList);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomPrice(@PathVariable Long id) {
        roomPriceService.deleteRoomPrice(id);
        return ResponseEntity.noContent().build();
    }
}
