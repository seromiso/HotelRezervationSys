package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.config.SecurityUtils;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.BadRequestException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.ConflictException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.mappers.HotelMapper;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelRegistrationRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelUpdateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.City;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.District;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.HotelStatus;
import com.finartzIntern.HotelRezervationSys.domain.repository.CityRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.DistrictRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.FileStorageService;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelManagerService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelManagerServiceImpl implements HotelManagerService {

    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;
    private final HotelMapper hotelMapper;
    private final FileStorageService fileStorageService;

    @Override
    public HotelResponseDto updateHotelInfo(Long hotelId, HotelUpdateRequestDto updateDto){
        Hotel existingHotel = hotelRepository.findById(hotelId).orElseThrow(() ->
                new ResourceNotFoundException("Otel bulunamadı !  Id : " + hotelId));
        if (updateDto.cityId() != null) {
            City newCity = cityRepository.findById(updateDto.cityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Şehir bulunamadı! ID: " + updateDto.cityId()));
            if(updateDto.districtId() == null){
                throw new ConflictException("Şehir değiştirildiğinde yeni bir ilçe seçilmesi zorunludur!");
            }
            existingHotel.setCity(newCity.getName());
        }
        if(updateDto.districtId() != null){
            Long targetCityId = (updateDto.cityId() !=null)
                    ? updateDto.cityId() : getCityIdByName(existingHotel.getCity());
            boolean isValid = districtRepository.existsByIdAndCityId(updateDto.districtId(), targetCityId);
            if(!isValid){
                throw new ConflictException("Seçilen ilçe belirtilen şehre ait değil!");
            }
            District newDistrict = districtRepository.findById(updateDto.districtId())
                    .orElseThrow(() -> new ResourceNotFoundException("İlçe bulunamadı! ID: " + updateDto.districtId()));

                existingHotel.setDistrict(newDistrict.getName());
        }
        updateHotelFieldsManually(existingHotel,updateDto);

         Hotel savedHotel = hotelRepository.save(existingHotel);

         return hotelMapper.toResponseDto(savedHotel);
    }


    public HotelResponseDto registerHotel(HotelRegistrationRequestDto dto){
        User hotelManager = SecurityUtils.getCurrentUser();
        City city = cityRepository.findById(dto.cityId())
                .orElseThrow(() -> new ResourceNotFoundException("Şehir bulunamadı! ID: " + dto.cityId()));
        boolean isDistrictValid = districtRepository.existsByIdAndCityId(dto.districtId(),dto.cityId());
        if (!isDistrictValid) {
            throw new BadRequestException("Seçilen ilçe belirtilen şehre ait değil!");
        }
        District district = districtRepository.findById(dto.districtId()).
                orElseThrow(() -> new ResourceNotFoundException("İlçe bulunamadı! ID: " + dto.districtId()));

        String documentUrl = fileStorageService.storeFile(dto.licenseDocument());

        //burası tek metot olmalı
        Hotel hotel = new Hotel();
        hotel.setName(dto.name());
        hotel.setCity(city.getName());
        hotel.setDistrict(district.getName());
        hotel.setAddress(dto.address());
        hotel.setPhone(dto.phone());
        hotel.setDescription(dto.description());
        hotel.setIbanNo(dto.ibanNo());
        hotel.setLicenseDocumentUrl(documentUrl);
        hotel.setManagerId(hotelManager.getId()); // JWT'den gelen kullanıcı direkt manager olarak atanıyor
        hotel.setStatus(HotelStatus.AWAITING_APPROVAL); // Admin onayı bekliyor
        hotel.setAverageRating(0.0);


        Hotel savedHotel = hotelRepository.save(hotel);

        return hotelMapper.toResponseDto(savedHotel);




     }
    private void updateHotelFieldsManually(Hotel hotel, HotelUpdateRequestDto dto) {
        if (dto.name() != null) {
            hotel.setName(dto.name());
        }
        if (dto.address() != null) {
            hotel.setAddress(dto.address());
        }
        if (dto.phone() != null) {
            hotel.setPhone(dto.phone());
        }
        if (dto.description() != null) {
            hotel.setDescription(dto.description());
        }
        if (dto.ibanNo() != null) {
            hotel.setIbanNo(dto.ibanNo());
        }
        if (dto.checkInTime() != null) {
            hotel.setCheckInTime(dto.checkInTime());
        }
        if (dto.checkOutTime() != null) {
            hotel.setCheckOutTime(dto.checkOutTime());
        }
    }
    private Long getCityIdByName(String cityName) {
        return cityRepository.findByName(cityName)
                .map(City::getId)
                .orElseThrow(() -> new ResourceNotFoundException("Mevcut şehrin ID'si bulunamadı: " + cityName));
    }




}