package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.FeaturesCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.FeaturesResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.FeatureCategory;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Features;
import com.finartzIntern.HotelRezervationSys.domain.repository.FeatureCategoryRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.FeaturesRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.FeaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeaturesServiceImpl implements FeaturesService {

    private final FeaturesRepository featuresRepository;
    private final FeatureCategoryRepository featureCategoryRepository;

    @Autowired
    public FeaturesServiceImpl(FeaturesRepository featuresRepository, FeatureCategoryRepository featureCategoryRepository) {
        this.featuresRepository = featuresRepository;
        this.featureCategoryRepository = featureCategoryRepository;
    }
    @Override
    @Transactional
    public FeaturesResponseDto createFeature(FeaturesCreateRequestDto requestDto) {


        FeatureCategory category = featureCategoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı, ID: " + requestDto.getCategoryId()));
        Features feature = new Features();
        feature.setName(requestDto.getName());
        feature.setType(requestDto.getType());
        feature.setCategory(category);

        Features savedFeature = featuresRepository.save(feature);

        return new FeaturesResponseDto(savedFeature.getId(), savedFeature.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeaturesResponseDto> getAllFeatures() {
        return featuresRepository.findAll().stream()
                .map(f -> new FeaturesResponseDto(f.getId(), f.getName()))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public FeaturesResponseDto getFeatureById(Long id) {

        Features features=featuresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Özellik bulunamadı, ID: " + id));

        return new FeaturesResponseDto(features.getId(), features.getName());
    }
}