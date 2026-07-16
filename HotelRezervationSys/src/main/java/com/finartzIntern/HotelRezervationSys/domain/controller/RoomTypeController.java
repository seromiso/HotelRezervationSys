    package com.finartzIntern.HotelRezervationSys.domain.controller;

    import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RoomTypeCreateRequestDto;
    import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.RoomTypeResponseDto;
    import com.finartzIntern.HotelRezervationSys.domain.service.RoomTypeService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/v1/room-types")
    @RequiredArgsConstructor

    public class RoomTypeController {
        private final RoomTypeService roomTypeService;

            @GetMapping("/hotel/{hotelId}")
        public ResponseEntity<List<RoomTypeResponseDto>> getRoomTypesByHotelId(@PathVariable Long hotelId) {
            List<RoomTypeResponseDto> responseList = roomTypeService.getRoomTypesByHotelId(hotelId);
            return ResponseEntity.ok(responseList);
        }
        @PostMapping("/hotel/{hotelId}")
        public ResponseEntity<RoomTypeResponseDto> createRoomType(
                @PathVariable Long hotelId,
                @RequestBody RoomTypeCreateRequestDto requestDto) {

            RoomTypeResponseDto response = roomTypeService.createRoomType(hotelId, requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<RoomTypeResponseDto> getRoomTypeById(@PathVariable Long id) {
            return ResponseEntity.ok(roomTypeService.getRoomTypeById(id));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteRoomType(@PathVariable Long id) {
            roomTypeService.deleteRoomType(id);
            return ResponseEntity.noContent().build();
        }
    }
