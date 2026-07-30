package com.finartzIntern.HotelRezervationSys.domain.service;

import java.time.LocalDate;

public interface AvailabilityService {
    boolean checkAvailability(Long roomTypeId, Integer totalInventory, LocalDate checkInDate, LocalDate checkOutDate);
}
