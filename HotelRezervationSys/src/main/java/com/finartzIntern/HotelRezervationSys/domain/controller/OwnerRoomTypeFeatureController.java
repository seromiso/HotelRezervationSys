package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.AddRoomTypeFeatureRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeFeatureResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.service.RoomTypeFeatureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/owner/room-types/{roomTypeId}/features")
public class OwnerRoomTypeFeatureController {

    private final RoomTypeFeatureService roomTypeFeatureService;

    public OwnerRoomTypeFeatureController(RoomTypeFeatureService roomTypeFeatureService) {
        this.roomTypeFeatureService = roomTypeFeatureService;
    }

    @PostMapping
    public ResponseEntity<RoomTypeFeatureResponseDto> addFeatureToRoomType(
            @PathVariable Long roomTypeId,
            @Valid @RequestBody AddRoomTypeFeatureRequestDto request,
            @AuthenticationPrincipal User currentUser
    ) {
        RoomTypeFeatureResponseDto createdFeature =
                roomTypeFeatureService.addFeatureToRoomTypeAsOwner(
                        roomTypeId,
                        request,
                        currentUser
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(createdFeature);
    }
}