package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import jakarta.validation.constraints.NotNull;

public record AddHotelFeatureRequestDto(@NotNull(message = "error.feature.id.required") Long featureId ) {
}
