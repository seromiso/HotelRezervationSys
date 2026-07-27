package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.ImageCategoryResponseDto;
import java.util.List;

public interface ImageCategoryService {
    List<ImageCategoryResponseDto> getAllCategories();
}
