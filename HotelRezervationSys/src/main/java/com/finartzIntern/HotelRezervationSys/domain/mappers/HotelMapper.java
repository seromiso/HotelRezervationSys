package com.finartzIntern.HotelRezervationSys.domain.mappers;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelUpdateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    HotelResponseDto toResponseDto(Hotel hotel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "managerId", ignore = true)
    @Mapping(target = "commissionRate",ignore = true)
    Hotel toEntityDto(HotelCreateRequestDto dto);

    void UpdateHotelFromDto(HotelUpdateRequestDto updateDto,@MappingTarget Hotel hotel);
}
