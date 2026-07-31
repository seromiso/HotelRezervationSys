package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.AddRoomTypeFeatureRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeFeatureResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.RoomTypeFeatureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room-types/{roomTypeId}/features")
public class RoomTypeFeatureController {

    private final RoomTypeFeatureService roomTypeFeatureService;

    public RoomTypeFeatureController(RoomTypeFeatureService roomTypeFeatureService) {
        this.roomTypeFeatureService = roomTypeFeatureService;
    }

    @GetMapping
    public ResponseEntity<List<RoomTypeFeatureResponseDto>> getFeaturesByRoomTypeId(
            @PathVariable Long roomTypeId
    ) {
        List<RoomTypeFeatureResponseDto> features =
                roomTypeFeatureService.getFeaturesByRoomTypeId(roomTypeId);

        return ResponseEntity.ok(features);
    }

    @GetMapping("/{featureId}")
    public ResponseEntity<RoomTypeFeatureResponseDto> getRoomTypeFeature(
            @PathVariable Long roomTypeId,
            @PathVariable Long featureId
    ) {
        RoomTypeFeatureResponseDto feature = roomTypeFeatureService.getRoomTypeFeature(roomTypeId, featureId);

        return ResponseEntity.ok(feature);
    }

    @GetMapping("/{featureId}/exists")
    public ResponseEntity<Boolean> hasFeature(
            @PathVariable Long roomTypeId,
            @PathVariable Long featureId
    ) {
        boolean exists = roomTypeFeatureService.hasFeature(roomTypeId, featureId);
        return ResponseEntity.ok(exists);
    }

    @PostMapping
    public ResponseEntity<RoomTypeFeatureResponseDto> addFeatureToRoomType(
            @PathVariable Long roomTypeId,
            @Valid @RequestBody AddRoomTypeFeatureRequestDto request
    ) {
        RoomTypeFeatureResponseDto createdFeature =
                roomTypeFeatureService.addFeatureToRoomType(roomTypeId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdFeature);
    }

    @DeleteMapping("/{featureId}")
    public ResponseEntity<Void> removeFeatureFromRoomType(
            @PathVariable Long roomTypeId,
            @PathVariable Long featureId
    ) {
        roomTypeFeatureService.removeFeatureFromRoomType(roomTypeId, featureId);
        return ResponseEntity.noContent().build();
    }
}