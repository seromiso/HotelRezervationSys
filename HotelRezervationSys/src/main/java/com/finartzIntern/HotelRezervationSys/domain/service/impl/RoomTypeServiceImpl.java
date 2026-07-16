package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RoomTypeCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomType;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelRepository;
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


    @Override // Sözleşmedeki metodu ezdiğimizi/uyguladığımızı belirtir
    @Transactional
    public RoomTypeResponseDto createRoomType(Long hotelId, RoomTypeCreateRequestDto requestDto) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Otel bulunamadı!"));

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
            throw new RuntimeException("Otel bulunamadı!");
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
    public RoomTypeResponseDto getRoomTypeById(Long id) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oda tipi bulunamadı!"));

        return new RoomTypeResponseDto(
                roomType.getId(),
                roomType.getTitle(),
                roomType.getMaxAdults(),
                roomType.getMaxChildren(),
                roomType.getBaseCapacity(),
                roomType.getBedConfiguration(),
                roomType.getTotalInventory(),
                roomType.getStatus(),
                roomType.getCreatedAt()
        );
    }

    @Override
    @Transactional
    public void deleteRoomType(Long id) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oda tipi bulunamadı!"));

        roomTypeRepository.delete(roomType);
    }
}
