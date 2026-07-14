package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelImageResponseDto {
    private Long id;
    private Long hotelId;
    private Long categoryId;
    private String categoryName;
    private String imageUrl;
    private Integer displayOrder;
}
