package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ConflictException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.InvalidRequestException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.AddRoomTypeFeatureRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeFeatureResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Features;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomType;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomTypeFeature;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.repository.FeaturesRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomTypeFeatureRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomTypeRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.RoomTypeFeatureService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class RoomTypeFeatureServiceImpl implements RoomTypeFeatureService {

    private final RoomTypeFeatureRepository roomTypeFeatureRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final FeaturesRepository featuresRepository;

    public RoomTypeFeatureServiceImpl(RoomTypeFeatureRepository roomTypeFeatureRepository,
                                      RoomTypeRepository roomTypeRepository,
                                      FeaturesRepository featuresRepository){
        this.roomTypeFeatureRepository = roomTypeFeatureRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.featuresRepository = featuresRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomTypeFeatureResponseDto> getFeaturesByRoomTypeId(Long roomTypeId) {
        return roomTypeFeatureRepository.findByRoomType_Id(roomTypeId)
                .stream()
                .map(RoomTypeFeatureResponseDto::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoomTypeFeatureResponseDto getRoomTypeFeature(Long roomTypeId, Long featureId) {
        RoomTypeFeature roomTypeFeature = roomTypeFeatureRepository
                .findByRoomType_IdAndFeature_Id(roomTypeId, featureId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "error.room.type.feature.not.found"
                ));

        return RoomTypeFeatureResponseDto.from(roomTypeFeature);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasFeature(Long roomTypeId, Long featureId) {
        return roomTypeFeatureRepository.existsByRoomType_IdAndFeature_Id(roomTypeId, featureId);
    }

    @Override
    @Transactional
    public RoomTypeFeatureResponseDto addFeatureToRoomType(
            Long roomTypeId,
            AddRoomTypeFeatureRequestDto request
    ) {
        if (roomTypeFeatureRepository.existsByRoomType_IdAndFeature_Id(roomTypeId, request.featureId())) {
            throw new ConflictException("error.room.type.feature.already.exists");
        }

        RoomType roomType = getRoomTypeOrThrow(roomTypeId);

        Features feature = getRoomFeatureToAddOrThrow(request.featureId());

        if (!"ROOM".equalsIgnoreCase(feature.getType())) {
            throw new InvalidRequestException("error.room.type.feature.only.room.allowed");
        }

        RoomTypeFeature roomTypeFeature = new RoomTypeFeature();
        roomTypeFeature.setRoomType(roomType);
        roomTypeFeature.setFeature(feature);

        RoomTypeFeature savedRoomTypeFeature = roomTypeFeatureRepository.save(roomTypeFeature);

        return RoomTypeFeatureResponseDto.from(savedRoomTypeFeature);
    }

    @Override
    @Transactional
    public void removeFeatureFromRoomType(Long roomTypeId, Long featureId) {
        if (!roomTypeFeatureRepository.existsByRoomType_IdAndFeature_Id(roomTypeId, featureId)) {
            throw new ResourceNotFoundException("error.room.type.feature.not.found");
        }

        roomTypeFeatureRepository.deleteByRoomType_IdAndFeature_Id(roomTypeId, featureId);
    }
    @Override
    @Transactional
    public RoomTypeFeatureResponseDto addFeatureToRoomTypeAsOwner(
            Long roomTypeId,
            AddRoomTypeFeatureRequestDto request,
            User currentUser
    ) {
        if (request == null || request.featureId() == null) {
            throw new InvalidRequestException("error.feature.id.required");
        }

        RoomType roomType = getRoomTypeOrThrow(roomTypeId);

        validateRoomTypeOwnership(roomType, currentUser);

        if (roomTypeFeatureRepository.existsByRoomType_IdAndFeature_Id(roomTypeId, request.featureId())) {
            throw new ConflictException("error.room.type.feature.already.exists");
        }

        Features feature = getRoomFeatureToAddOrThrow(request.featureId());

        RoomTypeFeature roomTypeFeature = new RoomTypeFeature();
        roomTypeFeature.setRoomType(roomType);
        roomTypeFeature.setFeature(feature);

        RoomTypeFeature savedRoomTypeFeature = roomTypeFeatureRepository.save(roomTypeFeature);

        return RoomTypeFeatureResponseDto.from(savedRoomTypeFeature);
    }

    private RoomType getRoomTypeOrThrow(Long roomTypeId) {
        return roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("error.room.type.not.found"));
    }

    private Features getRoomFeatureToAddOrThrow(Long featureId) {
        Features feature = featuresRepository.findById(featureId)
                .orElseThrow(() -> new ResourceNotFoundException("error.feature.not.found"));

        if (!"ROOM".equalsIgnoreCase(feature.getType())) {
            throw new InvalidRequestException("error.room.type.feature.only.room.allowed");
        }

        return feature;
    }

    private void validateRoomTypeOwnership(RoomType roomType, User currentUser) {
        if (currentUser == null) {
            throw new AccessDeniedException("error.authentication.required");
        }

        if (!Objects.equals(roomType.getHotel().getManagerId(), currentUser.getId())) {
            throw new AccessDeniedException("error.hotel.owner.forbidden");
        }
    }
}
