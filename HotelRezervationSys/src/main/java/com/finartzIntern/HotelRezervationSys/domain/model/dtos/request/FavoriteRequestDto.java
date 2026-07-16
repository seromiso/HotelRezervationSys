package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequestDto {
    private Long userId;
    private Long hotelId;
}
