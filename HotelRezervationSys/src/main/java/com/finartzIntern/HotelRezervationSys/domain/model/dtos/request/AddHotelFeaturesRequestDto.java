package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import jakarta.validation.constraints.NotEmpty;


import java.util.List;

public record AddHotelFeaturesRequestDto(
        @NotEmpty(message = "error.feature.id.required") List<Long> featureIds ) {
}