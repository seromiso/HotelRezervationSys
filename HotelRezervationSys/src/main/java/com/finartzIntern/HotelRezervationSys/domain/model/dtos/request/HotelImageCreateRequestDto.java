package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import lombok.Data;

@Data
public class HotelImageCreateRequestDto {

    private Long hotelId;
    private Long categoryId;
    private String imageUrl;
    private Integer displayOrder;
}
