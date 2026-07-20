package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponseDto {
    private Long id;
    private Long userId;
    private Long hotelId;
    private String hotelName;
}
