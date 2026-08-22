package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.model.enums.ReservationStatus;
import com.finartzIntern.HotelRezervationSys.domain.repository.ReservationRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final ReservationRepository reservationRepository;

    @Override
    public boolean checkAvailability(Long roomTypeId, Integer totalInventory, LocalDate checkInDate, LocalDate checkOutDate) {

        // Kendi yazdığın repository metodunu kullanarak o tarihlerde çakışan (dolu) oda sayısını buluyoruz.
        // NOT: Eğer ReservationStatus enumnın içinde CANCELLED yerine farklı bir isim varsa (örn: CANCELED), onu güncellemelisin.
        long overlappingReservations = reservationRepository.countOverlappingReservations(
                roomTypeId,
                checkInDate,
                checkOutDate,
                ReservationStatus.CANCELLED
        );

        // Toplam envanter, dolu odalardan fazlaysa demek ki elimizde boş oda var -> true döner.
        return totalInventory > overlappingReservations;
    }
}
