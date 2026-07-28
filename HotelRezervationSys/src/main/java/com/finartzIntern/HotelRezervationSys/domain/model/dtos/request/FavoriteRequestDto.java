package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequestDto {

    @NotNull(message = "Kullanıcı ID (userId) boş bırakılamaz!")
    private Long userId;

    @NotNull(message = "Otel ID (hotelId) boş bırakılamaz!")
    private Long hotelId;
}
