package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.AddHotelFeatureRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.FeaturesResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelFeatureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/v1/hotels/{hotelId}/features")
public class HotelFeatureController {

        private final HotelFeatureService hotelFeatureService;

        public HotelFeatureController(HotelFeatureService hotelFeatureService) {
            this.hotelFeatureService = hotelFeatureService;
        }

        @GetMapping
        public ResponseEntity<List<FeaturesResponseDto>> getFeaturesByHotelId(
                @PathVariable Long hotelId
        ) {
            List<FeaturesResponseDto> features = hotelFeatureService.getFeaturesByHotelId(hotelId);
            return ResponseEntity.ok(features);
        }

        @GetMapping("/{featureId}")
        public ResponseEntity<FeaturesResponseDto> getHotelFeature(
                @PathVariable Long hotelId,
                @PathVariable Long featureId
        ) {
            FeaturesResponseDto feature = hotelFeatureService.getHotelFeature(hotelId, featureId);
            return ResponseEntity.ok(feature);
        }

        @GetMapping("/{featureId}/exists")
        public ResponseEntity<Boolean> hasFeature(
                @PathVariable Long hotelId,
                @PathVariable Long featureId
        ) {
            boolean exists = hotelFeatureService.hasFeature(hotelId, featureId);
            return ResponseEntity.ok(exists);
        }

        @PostMapping
        public ResponseEntity<FeaturesResponseDto> addFeatureToHotel(
                @PathVariable Long hotelId,
                @Valid @RequestBody AddHotelFeatureRequestDto request
        ) {
            FeaturesResponseDto createdFeature = hotelFeatureService.addFeatureToHotel(hotelId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFeature);
        }

        @DeleteMapping("/{featureId}")
        public ResponseEntity<Void> removeFeatureFromHotel(
                @PathVariable Long hotelId,
                @PathVariable Long featureId
        ) {
            hotelFeatureService.removeFeatureFromHotel(hotelId, featureId);
            return ResponseEntity.noContent().build();
        }
}
