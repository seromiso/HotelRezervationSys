package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.ImageCategoryResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.ImageCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/image-categories")
@RequiredArgsConstructor
public class ImageCategoryController {

    private final ImageCategoryService imageCategoryService;

    @GetMapping
    public ResponseEntity<List<ImageCategoryResponseDto>> getAllCategories() {
        List<ImageCategoryResponseDto> response = imageCategoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }
}