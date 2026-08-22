package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ConflictException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.InvalidRequestException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.AddHotelFeatureRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.AddHotelFeaturesRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.FeaturesResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Features;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import com.finartzIntern.HotelRezervationSys.domain.repository.FeaturesRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelFeatureService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.AddHotelFeatureRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.Objects;

import java.util.List;

@Service
public class HotelFeatureServiceImpl implements HotelFeatureService {
    private final HotelRepository hotelRepository;
    private final FeaturesRepository featuresRepository;

    public HotelFeatureServiceImpl(
            HotelRepository hotelRepository,
            FeaturesRepository featuresRepository
    ) {
        this.hotelRepository = hotelRepository;
        this.featuresRepository = featuresRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeaturesResponseDto> getFeaturesByHotelId(Long hotelId) {
        Hotel hotel = getHotelOrThrow(hotelId);

        if (hotel.getFeatures() == null) {
            return List.of();
        }

        return hotel.getFeatures()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FeaturesResponseDto getHotelFeature(Long hotelId, Long featureId) {
        Hotel hotel = getHotelOrThrow(hotelId);

        Features feature = findFeatureInHotelOrThrow(hotel, featureId);

        return toResponseDto(feature);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasFeature(Long hotelId, Long featureId) {
        Hotel hotel = getHotelOrThrow(hotelId);

        return hotel.getFeatures() != null
                && hotel.getFeatures()
                .stream()
                .anyMatch(feature -> feature.getId().equals(featureId));
    }

    @Override
    @Transactional
    public FeaturesResponseDto addFeatureToHotel(
            Long hotelId,
            AddHotelFeatureRequestDto request
    ) {
        if (request == null || request.featureId() == null) {
            throw new InvalidRequestException("error.feature.id.required");
        }

        Hotel hotel = getHotelOrThrow(hotelId);

        Features feature = getHotelFeatureToAddOrThrow(hotel, request.featureId());

        hotel.addFeature(feature);
        hotelRepository.save(hotel);

        return toResponseDto(feature);
    }

    @Override
    @Transactional
    public void removeFeatureFromHotel(Long hotelId, Long featureId) {
        Hotel hotel = getHotelOrThrow(hotelId);

        Features feature = findFeatureInHotelOrThrow(hotel, featureId);

        hotel.deleteFeature(feature);
        hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    public List<FeaturesResponseDto> addFeaturesToHotelAsOwner(
            Long hotelId,
            AddHotelFeaturesRequestDto request,
            User currentUser
    ) {
        Hotel hotel = getHotelOrThrow(hotelId);

        validateHotelOwnership(hotel, currentUser);

        List<FeaturesResponseDto> addedFeatures = new ArrayList<>();

        for (Long featureId : request.featureIds()) {
            Features feature = getHotelFeatureToAddOrThrow(hotel, featureId);

            hotel.addFeature(feature);
            addedFeatures.add(toResponseDto(feature));
        }

        hotelRepository.save(hotel);

        return addedFeatures;
    }

    @Override
    @Transactional
    public void removeFeatureFromHotelAsOwner(
            Long hotelId,
            Long featureId,
            User currentUser
    ) {
        Hotel hotel = getHotelOrThrow(hotelId);

        validateHotelOwnership(hotel, currentUser);

        Features feature = findFeatureInHotelOrThrow(hotel, featureId);

        hotel.deleteFeature(feature);
        hotelRepository.save(hotel);
    }

    private Hotel getHotelOrThrow(Long hotelId) {
        return hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("error.hotel.not.found"));
    }

    private Features findFeatureInHotelOrThrow(Hotel hotel, Long featureId) {
        if (hotel.getFeatures() == null) {
            throw new ResourceNotFoundException("error.hotel.feature.not.found");
        }

        return hotel.getFeatures()
                .stream()
                .filter(feature -> feature.getId().equals(featureId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("error.hotel.feature.not.found"));
    }

    private FeaturesResponseDto toResponseDto(Features feature) {
        return new FeaturesResponseDto(
                feature.getId(),
                feature.getName(),
                feature.getCategory().getId()
        );
    }

    private void validateHotelOwnership(Hotel hotel, User currentUser) {
        if (currentUser == null) {
            throw new AccessDeniedException("error.authentication.required");
        }

        if (!Objects.equals(hotel.getManagerId(), currentUser.getId())) {
            throw new AccessDeniedException("error.hotel.owner.forbidden");
        }
    }
    private boolean isFeatureAlreadyInHotel(Hotel hotel, Long featureId) {
        return hotel.getFeatures() != null
                && hotel.getFeatures()
                .stream()
                .anyMatch(existingFeature -> existingFeature.getId().equals(featureId));
    }

    private Features getHotelFeatureToAddOrThrow(Hotel hotel, Long featureId) {
        Features feature = featuresRepository.findById(featureId)
                .orElseThrow(() -> new ResourceNotFoundException("error.feature.not.found"));

        if (!"HOTEL".equalsIgnoreCase(feature.getType())) {
            throw new InvalidRequestException("error.hotel.feature.only.hotel.allowed");
        }

        if (isFeatureAlreadyInHotel(hotel, feature.getId())) {
            throw new ConflictException("error.hotel.feature.already.exists");
        }

        return feature;
    }
}
