package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import jakarta.validation.constraints.NotNull;

public record AddRoomTypeFeatureRequestDto(@NotNull(message = "Feature id gerekli") Long featureId) {


}
