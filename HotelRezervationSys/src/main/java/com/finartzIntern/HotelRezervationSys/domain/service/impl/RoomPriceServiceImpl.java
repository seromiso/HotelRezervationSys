package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ConflictException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.InvalidRequestException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RoomPriceCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomPriceResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomPrice;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.RoomType;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomPriceRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.RoomTypeRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.RoomPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomPriceServiceImpl implements RoomPriceService {

    private final RoomPriceRepository roomPriceRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Override
    @Transactional
    public RoomPriceResponseDto addRoomPrice(Long roomTypeId, RoomPriceCreateRequestDto requestDto) {

        // 1. Tarih Mantık Kontrolü - İş Kuralı İhlali (HTTP 400)
        if (requestDto.endDate().isBefore(requestDto.startDate())) {
            throw new InvalidRequestException("Bitiş tarihi, başlangıç tarihinden önce olamaz!");
        }

        // Kaynak Bulunamadı (HTTP 404)
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Oda tipi bulunamadı!"));

        LocalDate newStart = requestDto.startDate();
        LocalDate newEnd = requestDto.endDate();

        // 2. Kesişen Tüm Fiyatları Bul
        List<RoomPrice> overlappingPrices = roomPriceRepository
                .findByRoomTypeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        roomTypeId, newEnd, newStart);

        // 3. İŞ KURALI: Eğer çakışan bir tarih aralığı varsa hata fırlat (HTTP 409 Conflict mantığı)
        if (!overlappingPrices.isEmpty()) {
            throw new ConflictException("Bu tarih aralığı mevcut bir fiyatlandırmayla çakışıyor!");
        }

        // 4. Yepyeni Fiyatımızı Kaydet
        RoomPrice newPrice = new RoomPrice();
        newPrice.setRoomType(roomType);
        newPrice.setStartDate(newStart);
        newPrice.setEndDate(newEnd);
        newPrice.setPricePerNight(requestDto.pricePerNight());
        newPrice.setCurrency(requestDto.currency());

        RoomPrice savedPrice = roomPriceRepository.save(newPrice);

        return new RoomPriceResponseDto(
                savedPrice.getId(),
                savedPrice.getRoomType().getId(),
                savedPrice.getStartDate(),
                savedPrice.getEndDate(),
                savedPrice.getPricePerNight(),
                savedPrice.getCurrency(),
                savedPrice.getCreatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomPriceResponseDto> getRoomPricesByRoomTypeId(Long roomTypeId) {

        List<RoomPrice> prices = roomPriceRepository.findByRoomTypeId(roomTypeId);

        return prices.stream().map(price -> new RoomPriceResponseDto(
                price.getId(),
                price.getRoomType().getId(),
                price.getStartDate(),
                price.getEndDate(),
                price.getPricePerNight(),
                price.getCurrency(),
                price.getCreatedAt()
        )).toList();
    }

    @Override
    @Transactional
    public void deleteRoomPrice(Long id) {
        RoomPrice roomPrice = roomPriceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Silinecek fiyat bulunamadı!"));
        roomPriceRepository.delete(roomPrice);
    }

    @Override
    @Transactional
    public RoomPriceResponseDto updateRoomPrice(Long priceId, RoomPriceCreateRequestDto requestDto) {

        // 1. Mevcut fiyatı veritabanından bul
        RoomPrice existingPrice = roomPriceRepository.findById(priceId)
                .orElseThrow(() -> new ResourceNotFoundException("Güncellenecek fiyat bulunamadı!"));

        // 2. Tarih mantık kontrolü
        if (requestDto.endDate().isBefore(requestDto.startDate())) {
            throw new InvalidRequestException("Bitiş tarihi, başlangıç tarihinden önce olamaz!");
        }

        // 3. Kesişen fiyatları bul (Mevcut repository metodunu tekrar kullanıyoruz)
        List<RoomPrice> overlappingPrices = roomPriceRepository
                .findByRoomTypeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        existingPrice.getRoomType().getId(), requestDto.endDate(), requestDto.startDate());

        // 4. İŞ KURALI: Kendisi HARİÇ, çakışan başka bir tarih aralığı var mı kontrolü
        boolean hasOverlap = overlappingPrices.stream()
                .anyMatch(p -> !p.getId().equals(priceId)); // Kendi ID'si dışındakilere bak

        if (hasOverlap) {
            throw new ConflictException("Güncellenen tarihler başka bir fiyatlandırmayla çakışıyor!");
        }

        // 5. Bilgileri güncelle ve kaydet
        existingPrice.setStartDate(requestDto.startDate());
        existingPrice.setEndDate(requestDto.endDate());
        existingPrice.setPricePerNight(requestDto.pricePerNight());
        existingPrice.setCurrency(requestDto.currency());

        RoomPrice updatedPrice = roomPriceRepository.save(existingPrice);

        return new RoomPriceResponseDto(
                updatedPrice.getId(),
                updatedPrice.getRoomType().getId(),
                updatedPrice.getStartDate(),
                updatedPrice.getEndDate(),
                updatedPrice.getPricePerNight(),
                updatedPrice.getCurrency(),
                updatedPrice.getCreatedAt()
        );
    }
}