package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelSearchCriteria;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelUpdateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelDetailResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelSearchResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.HotelStatus;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

   /* @GetMapping("/{id}")
    public ResponseEntity<HotelResponseDto> getHotelById(@PathVariable Long id){
        HotelResponseDto response = hotelService.getHotelById(id);
        return ResponseEntity.ok(response);

    }*/
    @GetMapping("/actives")
    public ResponseEntity<List<HotelResponseDto>> listActivehotels(){
        List<HotelResponseDto> hotels = hotelService.listActiveHotels();
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/search")
    public ResponseEntity<List<HotelSearchResponseDto>> searchHotel(@PathVariable HotelSearchCriteria criteria){
        List<HotelSearchResponseDto> response = hotelService.searchHotel(criteria);
        return ResponseEntity.ok(response);

    }@PostMapping
    public ResponseEntity<HotelResponseDto> createHotel(@Valid @RequestBody HotelCreateRequestDto requestDto) {
        HotelResponseDto createdHotel = hotelService.createHotel(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    // 2. Tüm Otelleri Listeleme (Admin panelleri vb. için)
    @GetMapping
    public ResponseEntity<List<HotelResponseDto>> listAllHotels() {
        return ResponseEntity.ok(hotelService.ListAllHotels());
    }

    // 3. Sadece Aktif Otelleri Listeleme
    @GetMapping("/active")
    public ResponseEntity<List<HotelResponseDto>> listActiveHotels() {
        return ResponseEntity.ok(hotelService.listActiveHotels());
    }

    @GetMapping("/by-city-and-status")
    public ResponseEntity<List<HotelResponseDto>> getHotelsByCityAndStatus(
            @RequestParam("city") String city,
            @RequestParam("status") HotelStatus status) {
        return ResponseEntity.ok(hotelService.getHotelsByCityAndStatus(city, status));
    }

    // 7. Yöneticiye (Manager) Göre Otelleri Getirme
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<HotelResponseDto>> getHotelsByManagerId(@PathVariable("managerId") Long managerId) {
        return ResponseEntity.ok(hotelService.getHotelsByManagerId(managerId));
    }

    // 8. Otel Bilgilerini Güncelleme (Tüm veya Parçalı Veri Güncelleme)
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponseDto> updateHotelInfo(
            @PathVariable("id") Long id,
            @Valid @RequestBody HotelUpdateRequestDto updateRequestDto) {
        return ResponseEntity.ok(hotelService.updateHotelInfo(id, updateRequestDto));
    }

    // 9. Sadece Otel Statüsünü Güncelleme (Örn: PASSIVE yapmak veya Onaylamak için)
    @PatchMapping("/{id}/status")
    public ResponseEntity<HotelResponseDto> updateHotelStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") HotelStatus status) {
        return ResponseEntity.ok(hotelService.updateHotelStatus(id, status));
    }
    @GetMapping("/{id}/inspect")
    public ResponseEntity<HotelDetailResponseDto> getHotelDetail(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelDetailsById(id));
    }












}
