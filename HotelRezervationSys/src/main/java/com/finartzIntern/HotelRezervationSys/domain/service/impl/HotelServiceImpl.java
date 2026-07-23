package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.mappers.HotelMapper;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelUpdateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.HotelStatus;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.RoomTypeStatus;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.AvailabilityService;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelService;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeFeatureResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeSearchResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomPrice;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomType;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomPriceRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {


    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    private final RoomTypeRepository roomTypeRepository;
    private final RoomPriceRepository roomPriceRepository;
    private final AvailabilityService availabilityService;

    @Override
    @Transactional(readOnly = true)
    public HotelResponseDto getHotelById(Long id) {

        Hotel hotel = hotelRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Otel Bulunamadı! Id: " + id));

        HotelResponseDto response = new HotelResponseDto();
        response = hotelMapper.toResponseDto(hotel);
        return response;

    }
    @Transactional(readOnly = true)
    @Override
    public List<HotelResponseDto> listActiveHotels() {
        List<Hotel> activeHotels = hotelRepository.findByStatus(HotelStatus.ACTIVE);

        return activeHotels.stream()
                .map(hotelMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    @Override
    public List<HotelResponseDto> getHotelsByCityAndStatus(String city, HotelStatus status) {
        List<Hotel> activeHotelsInCities = hotelRepository.findByCityAndStatus(city,status);
        return activeHotelsInCities.stream()
                .map(hotelMapper::toResponseDto).collect(Collectors.toList());


    }
    @Transactional(readOnly = true)
    @Override
    public List<HotelResponseDto> getHotelsByManagerId(Long id) {
        List<Hotel> HotelsByManager = hotelRepository.findByManagerId(id);
        return HotelsByManager.stream().map(hotelMapper::toResponseDto).collect(Collectors.toList());

    }
    @Transactional(readOnly = true)
    @Override
    public List<HotelResponseDto> ListAllHotels(){
    List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream()
                .map(hotelMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    @Override
    public HotelResponseDto createHotel(HotelCreateRequestDto createRequestDto){

        Hotel hotel = hotelMapper.toEntityDto(createRequestDto);
        Hotel savedHotel = hotelRepository.save(hotel);

        return hotelMapper.toResponseDto(savedHotel);


}

  @Override
  public HotelResponseDto updateHotelInfo(Long id, HotelUpdateRequestDto updateDto){

    Hotel existingHotel = hotelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Otel bulunamadı! ID: " + id));
    hotelMapper.UpdateHotelFromDto(updateDto,existingHotel);
    Hotel updatedHotel = hotelRepository.save(existingHotel);
    return hotelMapper.toResponseDto(updatedHotel);


 }
    @Override
    public HotelResponseDto updateHotelStatus(Long id, HotelStatus status) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Otel bulunamadı! ID: " + id));

        hotel.setStatus(status);
        Hotel updatedHotel = hotelRepository.save(hotel);
        return hotelMapper.toResponseDto(updatedHotel);
    }

    @Override
    public List<RoomTypeSearchResponseDto> searchAvailableRooms(
            Long hotelId, LocalDate checkIn, LocalDate checkOut, Integer adults, Integer children) {

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) {
            throw new IllegalArgumentException("Çıkış tarihi, giriş tarihinden önce veya aynı gün olamaz.");
        }

        List<RoomType> activeRooms = roomTypeRepository.findByHotelIdAndStatus(hotelId, RoomTypeStatus.ACTIVE);

        return activeRooms.stream()
                .filter(rt -> rt.getMaxAdults() >= adults && rt.getMaxChildren() >= children)
                .map(rt -> {
                    BigDecimal pricePerNight = roomPriceRepository
                            .findFirstByRoomTypeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                    rt.getId(), checkIn, checkOut)
                            .map(RoomPrice::getPricePerNight)
                            .orElse(BigDecimal.ZERO);

                    BigDecimal totalPrice = pricePerNight.multiply(BigDecimal.valueOf(nights));

                    boolean isAvailable = availabilityService.checkAvailability(
                            rt.getId(), rt.getTotalInventory(), checkIn, checkOut);

                    String coverImage = rt.getImages().isEmpty() ? null : rt.getImages().get(0).getImageUrl();
                    List<RoomTypeFeatureResponseDto> features = rt.getFeatures().stream()
                            .map(RoomTypeFeatureResponseDto::from)
                            .toList();

                    return new RoomTypeSearchResponseDto(
                            rt.getId(),
                            rt.getTitle(),
                            rt.getMaxAdults(),
                            rt.getMaxChildren(),
                            features,
                            coverImage,
                            totalPrice,
                            isAvailable
                    );
                })
                .toList();
    }

}