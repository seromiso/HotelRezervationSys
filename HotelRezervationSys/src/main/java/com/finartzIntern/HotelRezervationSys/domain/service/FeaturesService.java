package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.FeaturesCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.FeaturesResponseDto;

import java.util.List;

public interface FeaturesService {

    FeaturesResponseDto createFeature(FeaturesCreateRequestDto requestDto);

    List<FeaturesResponseDto> getAllFeatures();

    FeaturesResponseDto getFeatureById(Long id);
}