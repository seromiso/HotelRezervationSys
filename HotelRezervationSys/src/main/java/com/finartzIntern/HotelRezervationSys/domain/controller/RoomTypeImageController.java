package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RoomTypeImageCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeImageResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.RoomTypeImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room-type-images")
@RequiredArgsConstructor
public class RoomTypeImageController {

    private final RoomTypeImageService roomTypeImageService;


    @GetMapping("/room-type/{roomTypeId}")
    public ResponseEntity<List<RoomTypeImageResponseDto>> getImagesByRoomTypeId(@PathVariable Long roomTypeId) {
        return ResponseEntity.ok(roomTypeImageService.getImagesByRoomTypeId(roomTypeId));
    }


    @PostMapping("/room-type/{roomTypeId}")
    public ResponseEntity<RoomTypeImageResponseDto> addImageToRoomType(
            @PathVariable Long roomTypeId,
            @RequestBody RoomTypeImageCreateRequestDto requestDto) {

        RoomTypeImageResponseDto response = roomTypeImageService.addImageToRoomType(roomTypeId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        roomTypeImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}