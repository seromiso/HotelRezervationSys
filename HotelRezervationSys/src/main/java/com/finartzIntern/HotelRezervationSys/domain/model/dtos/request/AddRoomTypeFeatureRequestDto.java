package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import jakarta.validation.constraints.NotNull;

public record AddRoomTypeFeatureRequestDto(@NotNull(message = "error.feature.id.required") Long featureId) {


}
