package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ConflictException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.AddRoomTypeFeatureRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeFeatureResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Features;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomType;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomTypeFeature;
import com.finartzIntern.HotelRezervationSys.domain.repository.FeaturesRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomTypeFeatureRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomTypeRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.RoomTypeFeatureService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                        "Feature not found for room type id: " + roomTypeId
                                + ", feature id: " + featureId
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
            throw new ConflictException(
                    "This feature already exists for room type id: " + roomTypeId
            );
        }

        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Room type not found with id: " + roomTypeId
                ));

        Features feature = featuresRepository.findById(request.featureId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Feature not found with id: " + request.featureId()
                ));

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
            throw new ResourceNotFoundException(
                    "Feature not found for room type id: " + roomTypeId
                            + ", feature id: " + featureId
            );
        }

        roomTypeFeatureRepository.deleteByRoomType_IdAndFeature_Id(roomTypeId, featureId);
    }
}
