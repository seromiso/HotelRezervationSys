package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RoomTypeImageCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeImageResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomType;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomTypeImage;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomTypeImageRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomTypeRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.RoomTypeImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeImageServiceImpl implements RoomTypeImageService {

    private final RoomTypeImageRepository roomTypeImageRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Override
    @Transactional
    public RoomTypeImageResponseDto addImageToRoomType(Long roomTypeId, RoomTypeImageCreateRequestDto requestDto) {

        // Kaynak Bulunamadı (HTTP 404)
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resim eklenecek oda tipi bulunamadı!"));

        RoomTypeImage image = new RoomTypeImage();
        image.setRoomType(roomType);
        image.setImageUrl(requestDto.imageUrl());
        image.setDisplayOrder(requestDto.displayOrder());

        RoomTypeImage savedImage = roomTypeImageRepository.save(image);

        return new RoomTypeImageResponseDto(
                savedImage.getId(),
                savedImage.getRoomType().getId(),
                savedImage.getImageUrl(),
                savedImage.getDisplayOrder(),
                savedImage.getCreatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomTypeImageResponseDto> getImagesByRoomTypeId(Long roomTypeId) {

        // Kaynak Bulunamadı (HTTP 404)
        if (!roomTypeRepository.existsById(roomTypeId)) {
            throw new ResourceNotFoundException("Oda tipi bulunamadı!");
        }

        List<RoomTypeImage> images = roomTypeImageRepository.findByRoomTypeIdOrderByDisplayOrderAsc(roomTypeId);

        return images.stream().map(image -> new RoomTypeImageResponseDto(
                image.getId(),
                image.getRoomType().getId(),
                image.getImageUrl(),
                image.getDisplayOrder(),
                image.getCreatedAt()
        )).toList();
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) {

        // Kaynak Bulunamadı (HTTP 404)
        RoomTypeImage image = roomTypeImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Silinecek resim bulunamadı!"));

        roomTypeImageRepository.delete(image);
    }
}