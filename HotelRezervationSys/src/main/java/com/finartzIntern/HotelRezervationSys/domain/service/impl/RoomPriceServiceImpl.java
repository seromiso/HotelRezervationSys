package com.finartzIntern.HotelRezervationSys.domain.service.impl;

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
import java.util.ArrayList;
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

        List<RoomPrice> pricesToDelete = new ArrayList<>();
        List<RoomPrice> pricesToSave = new ArrayList<>();

        // 3. PARÇALAMA ALGORİTMASI
        for (RoomPrice existing : overlappingPrices) {
            boolean startsBefore = existing.getStartDate().isBefore(newStart);
            boolean endsAfter = existing.getEndDate().isAfter(newEnd);

            if (startsBefore && endsAfter) {
                // İhtimal 1: İÇİNE DÜŞME (Ortadan Bölünme)

                // Sağ tarafı yeni bir kayıt olarak oluştur
                RoomPrice rightPart = new RoomPrice();
                rightPart.setRoomType(roomType);
                rightPart.setCurrency(existing.getCurrency());
                rightPart.setPricePerNight(existing.getPricePerNight());
                rightPart.setStartDate(newEnd.plusDays(1)); // Yeni fiyatın bittiği günün ertesi
                rightPart.setEndDate(existing.getEndDate());
                pricesToSave.add(rightPart);

                // Sol taraf için mevcut kaydı güncelle
                existing.setEndDate(newStart.minusDays(1)); // Yeni fiyatın başladığı günden bir önceki gün
                pricesToSave.add(existing);

            } else if (startsBefore && !endsAfter) {
                // İhtimal 2: SAĞDAN EZME
                existing.setEndDate(newStart.minusDays(1));
                pricesToSave.add(existing);

            } else if (!startsBefore && endsAfter) {
                // İhtimal 3: SOLDAN EZME
                existing.setStartDate(newEnd.plusDays(1));
                pricesToSave.add(existing);

            } else {
                // İhtimal 4: TAMAMEN YUTMA
                pricesToDelete.add(existing);
            }
        }

        // 4. Değişiklikleri Veritabanına Yansıt
        roomPriceRepository.deleteAll(pricesToDelete);
        roomPriceRepository.saveAll(pricesToSave);

        // 5. Yepyeni Fiyatımızı Kaydet
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
}
