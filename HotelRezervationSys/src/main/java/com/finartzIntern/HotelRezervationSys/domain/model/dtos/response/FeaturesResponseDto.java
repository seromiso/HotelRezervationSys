package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeaturesResponseDto {
    private Long id;
    private String name;
    private Long categoryId;
}
