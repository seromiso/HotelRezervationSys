package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import lombok.Data;

@Data
public class FeatureCategoryCreateRequestDto {
    private String name;
    private String type;
}
