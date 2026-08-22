package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ConflictException;
import com.finartzIntern.HotelRezervationSys.domain.mappers.HotelMapper;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelSearchCriteria;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelUpdateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelDetailResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelSearchResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Features;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.HotelImage;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.HotelStatus;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.ReservationStatus;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserRole;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.UserRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {


    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final UserRepository userRepository;

   /* @Override
    @Transactional(readOnly = true)
    public HotelResponseDto getHotelById(Long id) {

        Hotel hotel = hotelRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Otel Bulunamadı! Id: " + id));

        HotelResponseDto response = new HotelResponseDto();
        response = hotelMapper.toResponseDto(hotel);
        return response;

    }*/
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
    @Transactional
    public HotelResponseDto createHotel(HotelCreateRequestDto requestDto) {
        //  Manager'ı veritabanından buluyoruz
        User manager = userRepository.findById(requestDto.managerId())
                .orElseThrow(() -> new EntityNotFoundException("Manager bulunamadı: " + requestDto.managerId()));

        Hotel hotel = new Hotel();
        hotel.setName(requestDto.name());
        hotel.setCity(requestDto.city());
        hotel.setDistrict(requestDto.district());
        hotel.setAddress(requestDto.address());
        hotel.setPhone(requestDto.phone());
        hotel.setDescription(requestDto.description());
        hotel.setManagerId(manager.getId());
        hotel.setIbanNo(requestDto.ibanNo());
        hotel.setStatus(HotelStatus.AWAITING_APPROVAL);
        hotel.setAverageRating(0.0);

        Hotel savedHotel = hotelRepository.save(hotel);


        return mapToHotelResponseDto(savedHotel);
    }


    public HotelResponseDto mapToHotelResponseDto(Hotel hotel) {
        return new HotelResponseDto(
                hotel.getId(),
                hotel.getManagerId(),
                hotel.getName(),
                hotel.getCity(),
                hotel.getDistrict(),
                hotel.getAddress(),
                hotel.getPhone(),
                hotel.getDescription(),
                hotel.getCommissionRate(),
                hotel.getIbanNo(),
                hotel.getStatus(),
                hotel.getCheckInTime(),
                hotel.getCheckOutTime(),
                hotel.getUpdatedAt(),
                hotel.getCreatedAt()

        );
    }
  @Override
  public HotelResponseDto updateHotelInfo(Long id, HotelUpdateRequestDto updateDto){

    Hotel existingHotel = hotelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Otel bulunamadı! ID: " + id));
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
    @Transactional(readOnly = true)
    public List<HotelSearchResponseDto> searchHotel(HotelSearchCriteria criteria) {
        if (!criteria.checkOutDate().isAfter(criteria.checkInDate())) {
            throw new ConflictException(
                    "Check-in tarihi, check-out tarihinden sonra olamaz."
            );
        }
        List<Hotel> hotels = hotelRepository.searchHotels(
                criteria.city(),
                criteria.district(),
                criteria.checkInDate(),
                criteria.checkOutDate(),
                criteria.guestCount(),
                HotelStatus.ACTIVE,
                ReservationStatus.CANCELLED
        );

        return hotels.stream()
                .map(hotel -> new HotelSearchResponseDto(
                        hotel.getId(),
                        hotel.getName(),
                        hotel.getCity(),
                        hotel.getDistrict(),
                        hotel.getAddress(),
                        hotel.getAverageRating(),
                        hotel.getCoverImageUrl()
                ))
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public HotelDetailResponseDto getHotelDetailsById(Long id){
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Otel bulunamadı!"));

        if (hotel.getStatus() == HotelStatus.INACTIVE) {
            throw new ResourceNotFoundException("Otel bulunamadı!");
        }
        Set<String> featureSet = hotel.getFeatures().stream().map(Features::getName)
                .collect(Collectors.toSet());

        return new HotelDetailResponseDto(hotel.getId(),
                hotel.getName(),
                hotel.getCity(),
                hotel.getDistrict(),
                hotel.getAddress(),
                hotel.getDescription(),
                featureSet,
                hotel.getAverageRating());
       }

    @Transactional
    public void approveHotelRegistration(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Otel bulunamadı!"));

        // Oteli aktif et (İlana çıkar)
        hotel.setStatus(HotelStatus.ACTIVE);

        // Otel sahibinin rolünü CUSTOMER -> MANAGER yap
        Long ownerId = hotel.getManagerId();
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Otel yöneticisi bulunamadı! ID: " + ownerId));
        if (owner.getRole() != UserRole.MANAGER) {
            owner.setRole(UserRole.MANAGER);
            userRepository.save(owner);
        }

        hotelRepository.save(hotel);
    }




}