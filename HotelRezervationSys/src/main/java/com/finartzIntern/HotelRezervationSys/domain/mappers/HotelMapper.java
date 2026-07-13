package com.finartzIntern.HotelRezervationSys.domain.mappers;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    HotelResponseDto toResponseDto(Hotel hotel);
    Hotel toEntityDto(HotelCreateRequestDto dto);
}
