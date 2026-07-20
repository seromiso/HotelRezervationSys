package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.FeaturesCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.FeaturesResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.UserResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.FeaturesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/features")
@RequiredArgsConstructor
public class    FeaturesController {

    private final FeaturesService featuresService;

    @PostMapping
    public ResponseEntity<FeaturesResponseDto> createFeature(@RequestBody FeaturesCreateRequestDto requestDto) {
        FeaturesResponseDto response = featuresService.createFeature(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("")
    public ResponseEntity<List<FeaturesResponseDto>> getAllFeatures() {
        List<FeaturesResponseDto> response = featuresService.getAllFeatures();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeaturesResponseDto> getUserById(@PathVariable Long id) {
        FeaturesResponseDto response = featuresService.getFeatureById(id);
        return ResponseEntity.ok(response);
    }
}