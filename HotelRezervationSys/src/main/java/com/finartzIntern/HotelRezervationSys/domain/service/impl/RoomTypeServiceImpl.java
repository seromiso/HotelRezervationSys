package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RoomTypeCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeDetailResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeFeatureResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeImageResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomType;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomTypeFeatureRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomTypeImageRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomTypeRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.RoomTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final HotelRepository hotelRepository;

    private final RoomTypeImageRepository roomTypeImageRepository;
    private final RoomTypeFeatureRepository roomTypeFeatureRepository;

    @Override
    @Transactional
    public RoomTypeResponseDto createRoomType(Long hotelId, RoomTypeCreateRequestDto requestDto) {

        // Kaynak Bulunamadı (HTTP 404)
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Otel bulunamadı!"));

        RoomType roomType = new RoomType();
        roomType.setHotel(hotel);
        roomType.setTitle(requestDto.title());
        roomType.setMaxAdults(requestDto.maxAdults());
        roomType.setMaxChildren(requestDto.maxChildren());
        roomType.setBaseCapacity(requestDto.baseCapacity());
        roomType.setBedConfiguration(requestDto.bedConfiguration());
        roomType.setTotalInventory(requestDto.totalInventory());

        roomType.activateRoomType();

        RoomType savedRoomType = roomTypeRepository.save(roomType);

        return new RoomTypeResponseDto(
                savedRoomType.getId(),
                savedRoomType.getTitle(),
                savedRoomType.getMaxAdults(),
                savedRoomType.getMaxChildren(),
                savedRoomType.getBaseCapacity(),
                savedRoomType.getBedConfiguration(),
                savedRoomType.getTotalInventory(),
                savedRoomType.getStatus(),
                savedRoomType.getCreatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomTypeResponseDto> getRoomTypesByHotelId(Long hotelId) {

        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("Otel bulunamadı!");
        }

        List<RoomType> roomTypes = roomTypeRepository.findByHotelId(hotelId);

        return roomTypes.stream().map(roomType -> new RoomTypeResponseDto(
                roomType.getId(),
                roomType.getTitle(),
                roomType.getMaxAdults(),
                roomType.getMaxChildren(),
                roomType.getBaseCapacity(),
                roomType.getBedConfiguration(),
                roomType.getTotalInventory(),
                roomType.getStatus(),
                roomType.getCreatedAt()
        )).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoomTypeDetailResponseDto getRoomTypeById(Long id) {

        // 1. Temel Oda Bilgisini Getir
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oda tipi bulunamadı!"));

        // 2. Odaya Ait Resimleri (Galeriyi) Çek
        List<RoomTypeImageResponseDto> gallery = roomTypeImageRepository
                .findByRoomTypeIdOrderByDisplayOrderAsc(id)
                .stream()
                .map(img -> new RoomTypeImageResponseDto(
                        img.getId(), img.getRoomType().getId(), img.getImageUrl(),
                        img.getDisplayOrder(), img.getCreatedAt()
                )).toList();

        // 3. Odaya Ait Özellikleri Çek (Kendi yazdığın muazzam from() metodunu kullandık)
        List<RoomTypeFeatureResponseDto> features = roomTypeFeatureRepository
                .findByRoomType_Id(id)
                .stream()
                .map(RoomTypeFeatureResponseDto::from)
                .toList();

        // 4. Her Şeyi Tek Bir Pakette (Detail DTO) Birleştirip Dön
        return new RoomTypeDetailResponseDto(
                roomType.getId(),
                roomType.getTitle(),
                roomType.getDescription(), // Entity'e eklediğimiz alan
                roomType.getMaxAdults(),
                roomType.getMaxChildren(),
                roomType.getBaseCapacity(),
                roomType.getBedConfiguration(),
                roomType.getTotalInventory(),
                roomType.getStatus(),
                roomType.getCreatedAt(),
                gallery,
                features
        );
    }

    @Override
    @Transactional
    public void deleteRoomType(Long id) {

        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oda tipi bulunamadı!"));

        roomTypeRepository.delete(roomType);
    }
}