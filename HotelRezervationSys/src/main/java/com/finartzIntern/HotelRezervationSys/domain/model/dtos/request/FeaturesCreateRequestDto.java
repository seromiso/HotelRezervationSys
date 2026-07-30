package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import lombok.Data;

@Data
public class FeaturesCreateRequestDto {
    private Long categoryId;
    private String name;
    private String type;
}
