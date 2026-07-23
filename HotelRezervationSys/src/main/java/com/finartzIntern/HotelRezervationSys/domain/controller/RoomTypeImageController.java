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
@RequestMapping("/api/v1/room-types")
@RequiredArgsConstructor
public class RoomTypeImageController {

    private final RoomTypeImageService roomTypeImageService;

    @GetMapping("/{id}/images")
    public ResponseEntity<List<RoomTypeImageResponseDto>> getImagesByRoomTypeId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roomTypeImageService.getImagesByRoomTypeId(id));
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<RoomTypeImageResponseDto> addImageToRoomType(
            @PathVariable("id") Long id,
            @RequestBody RoomTypeImageCreateRequestDto requestDto) {

        RoomTypeImageResponseDto response = roomTypeImageService.addImageToRoomType(id, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        roomTypeImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}
