package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.ImageCategoryResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.repository.ImageCategoryRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.ImageCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageCategoryServiceImpl implements ImageCategoryService {

    private final ImageCategoryRepository imageCategoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ImageCategoryResponseDto> getAllCategories() {
        return imageCategoryRepository.findAll().stream()
                .map(category -> new ImageCategoryResponseDto(
                        category.getId(),
                        category.getName()
                ))
                .collect(Collectors.toList());
    }
}