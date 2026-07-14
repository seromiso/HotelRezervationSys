package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;



public record CreateRoomTypeRequestDto(

        String title,
        Integer maxAdults,
        Integer maxChildren,
        Integer baseCapacity,
        String bedConfiguration,
        Integer totalInventory
) {
}
