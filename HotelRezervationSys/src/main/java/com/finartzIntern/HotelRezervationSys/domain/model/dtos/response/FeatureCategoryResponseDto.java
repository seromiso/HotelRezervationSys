package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureCategoryResponseDto {
    private Long id;
    private String name;
}
