package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RoomTypeCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/owner") // Belgenin istediği owner takısı
@RequiredArgsConstructor
public class OwnerRoomTypeController {

    private final RoomTypeService roomTypeService;

    @GetMapping("/hotels/{hotelId}/room-types")
    public ResponseEntity<java.util.List<RoomTypeResponseDto>> getRoomTypesByHotelId(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomTypeService.getRoomTypesByHotelId(hotelId));
    }

    @PostMapping("/hotels/{hotelId}/room-types")
    public ResponseEntity<RoomTypeResponseDto> createRoomType(
            @PathVariable Long hotelId,
            @RequestBody RoomTypeCreateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomTypeService.createRoomType(hotelId, requestDto));
    }

    @PatchMapping("/room-types/{id}")
    public ResponseEntity<RoomTypeResponseDto> updateRoomType(
            @PathVariable Long id,
            @RequestBody RoomTypeCreateRequestDto requestDto) {
        return ResponseEntity.ok(roomTypeService.updateRoomType(id, requestDto));
    }

    @DeleteMapping("/room-types/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Long id) {
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build();
    }
}