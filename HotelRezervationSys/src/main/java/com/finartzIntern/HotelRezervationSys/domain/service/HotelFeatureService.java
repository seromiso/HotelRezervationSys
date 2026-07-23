package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.AddHotelFeatureRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.FeaturesResponseDto;

import java.util.List;

public interface HotelFeatureService {

    List<FeaturesResponseDto> getFeaturesByHotelId(Long hotelId);
    FeaturesResponseDto getHotelFeature(Long hotelId, Long featureId);
    boolean hasFeature(Long hotelId, Long featureId);
    FeaturesResponseDto addFeatureToHotel(Long hotelId, AddHotelFeatureRequestDto request);
    void removeFeatureFromHotel(Long hotelId, Long featureId);
}
