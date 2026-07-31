package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.AddHotelFeaturesRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.FeaturesResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelFeatureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/owner/hotels/{hotelId}/features")
public class OwnerHotelFeatureController {

    private final HotelFeatureService hotelFeatureService;

    public OwnerHotelFeatureController(HotelFeatureService hotelFeatureService) {
        this.hotelFeatureService = hotelFeatureService;
    }

    @PostMapping
    public ResponseEntity<List<FeaturesResponseDto>> addFeaturesToHotel(
            @PathVariable Long hotelId,
            @Valid @RequestBody AddHotelFeaturesRequestDto request,
            @AuthenticationPrincipal User currentUser
    ) {
        List<FeaturesResponseDto> addedFeatures =
                hotelFeatureService.addFeaturesToHotelAsOwner(
                        hotelId,
                        request,
                        currentUser
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(addedFeatures);
    }

    @DeleteMapping("/{featureId}")
    public ResponseEntity<Void> removeFeatureFromHotel(
            @PathVariable Long hotelId,
            @PathVariable Long featureId,
            @AuthenticationPrincipal User currentUser
    ) {
        hotelFeatureService.removeFeatureFromHotelAsOwner(
                hotelId,
                featureId,
                currentUser
        );

        return ResponseEntity.noContent().build();
    }
}
