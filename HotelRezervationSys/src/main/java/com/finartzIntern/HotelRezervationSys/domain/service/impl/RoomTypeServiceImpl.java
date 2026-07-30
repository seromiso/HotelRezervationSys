package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ConflictException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.InvalidRequestException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RoomTypeCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeDetailResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeFeatureResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeImageResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeSearchResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomPrice;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomType;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomTypeImage;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.ReservationStatus;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.ReservationRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomPriceRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomTypeFeatureRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomTypeImageRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomTypeRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.AvailabilityService;
import com.finartzIntern.HotelRezervationSys.domain.service.RoomTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final HotelRepository hotelRepository;
    private final RoomTypeImageRepository roomTypeImageRepository;
    private final RoomTypeFeatureRepository roomTypeFeatureRepository;
    private final ReservationRepository reservationRepository;

    // Müşteri Arama Motoru İçin Eklenen Bağımlılıklar
    private final AvailabilityService availabilityService;
    private final RoomPriceRepository roomPriceRepository;

    @Override
    @Transactional
    public RoomTypeResponseDto createRoomType(Long hotelId, RoomTypeCreateRequestDto requestDto) {

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

        return roomTypes.stream()
                .map(roomType -> new RoomTypeResponseDto(
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

    // YENİ VE KAPSAMLI ARAMA MOTORU (Eski listeleme metodunun yerine geldi)
    @Override
    @Transactional(readOnly = true)
    public List<RoomTypeSearchResponseDto> searchRoomTypes(Long hotelId, LocalDate checkIn, LocalDate checkOut, Integer adults, Integer children) {

        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("Otel bulunamadı!");
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) {
            throw new InvalidRequestException("Çıkış tarihi giriş tarihinden sonra olmalıdır!");
        }

        List<RoomType> roomTypes = roomTypeRepository.findByHotelId(hotelId);

        return roomTypes.stream()
                .filter(rt -> rt.getMaxAdults() >= adults && rt.getMaxChildren() >= children)
                .map(roomType -> {

                    boolean isAvailable = availabilityService.checkAvailability(
                            roomType.getId(), roomType.getTotalInventory(), checkIn, checkOut);

                    BigDecimal totalPrice = BigDecimal.ZERO;
                    Optional<RoomPrice> priceOpt = roomPriceRepository
                            .findFirstByRoomTypeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                    roomType.getId(), checkOut, checkIn);

                    if (priceOpt.isPresent()) {
                        totalPrice = priceOpt.get().getPricePerNight().multiply(BigDecimal.valueOf(nights));
                    }

                    String coverImage = roomTypeImageRepository.findByRoomTypeIdOrderByDisplayOrderAsc(roomType.getId())
                            .stream().findFirst().map(RoomTypeImage::getImageUrl).orElse(null);

                    List<RoomTypeFeatureResponseDto> features = roomTypeFeatureRepository.findByRoomTypeId(roomType.getId())
                            .stream().map(RoomTypeFeatureResponseDto::from).toList();

                    return new RoomTypeSearchResponseDto(
                            roomType.getId(),
                            roomType.getTitle(),
                            roomType.getMaxAdults(),
                            roomType.getMaxChildren(),
                            features,
                            coverImage,
                            totalPrice,
                            isAvailable
                    );
                }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoomTypeDetailResponseDto getRoomTypeById(Long id) {

        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oda tipi bulunamadı!"));

        List<RoomTypeImageResponseDto> gallery = roomTypeImageRepository
                .findByRoomTypeIdOrderByDisplayOrderAsc(id)
                .stream()
                .map(img -> new RoomTypeImageResponseDto(
                        img.getId(), img.getRoomType().getId(), img.getImageUrl(),
                        img.getDisplayOrder(), img.getCreatedAt()
                )).toList();

        List<RoomTypeFeatureResponseDto> features = roomTypeFeatureRepository
                .findByRoomType_Id(id)
                .stream()
                .map(RoomTypeFeatureResponseDto::from)
                .toList();

        return new RoomTypeDetailResponseDto(
                roomType.getId(),
                roomType.getTitle(),
                roomType.getDescription(),
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
    public RoomTypeResponseDto updateRoomType(Long id, RoomTypeCreateRequestDto requestDto) {

        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Güncellenecek oda tipi bulunamadı!"));

        if (requestDto.totalInventory() < roomType.getTotalInventory()) {

            int activeReservations = reservationRepository.countActiveReservationsByRoomTypeId(
                    id,
                    ReservationStatus.CANCELLED
            );

            if (requestDto.totalInventory() < activeReservations) {
                throw new ConflictException(
                        "Oda sayısı " + activeReservations + " değerinden aza düşürülemez! İçeride aktif rezervasyonlar var."
                );
            }
        }

        roomType.setTitle(requestDto.title());
        roomType.setMaxAdults(requestDto.maxAdults());
        roomType.setMaxChildren(requestDto.maxChildren());
        roomType.setBaseCapacity(requestDto.baseCapacity());
        roomType.setBedConfiguration(requestDto.bedConfiguration());
        roomType.setTotalInventory(requestDto.totalInventory());

        RoomType updatedRoomType = roomTypeRepository.save(roomType);

        return new RoomTypeResponseDto(
                updatedRoomType.getId(),
                updatedRoomType.getTitle(),
                updatedRoomType.getMaxAdults(),
                updatedRoomType.getMaxChildren(),
                updatedRoomType.getBaseCapacity(),
                updatedRoomType.getBedConfiguration(),
                updatedRoomType.getTotalInventory(),
                updatedRoomType.getStatus(),
                updatedRoomType.getCreatedAt()
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