package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.InvalidRequestException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.HotelImageCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.HotelImage;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.ImageCategory;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelImageRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.ImageCategoryRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HotelImageServiceImpl implements HotelImageService {

    private final HotelRepository hotelRepository;
    private final ImageCategoryRepository categoryRepository;
    private final HotelImageRepository hotelImageRepository;

    @Override
    @Transactional
    public void addImagesToHotel(Long hotelId, HotelImageCreateRequestDto requestDto, Long currentUserId) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("error.hotel.not.found"));

        if (!hotel.getManagerId().equals(currentUserId)) {
            throw new InvalidRequestException("error.hotel.image.unauthorized");
        }


        for (HotelImageCreateRequestDto.ImageItem item : requestDto.getImages()) {

            ImageCategory category = categoryRepository.findById(item.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("error.category.not.found"));

            HotelImage hotelImage = new HotelImage();
            hotelImage.setHotel(hotel);
            hotelImage.setCategory(category);
            hotelImage.setImageUrl(item.getUrl());

            if (item.getDisplayOrder() != null) {
                hotelImage.setDisplayOrder(item.getDisplayOrder());
            }

            hotelImageRepository.save(hotelImage);
        }
    }
    @Override
    @Transactional
    public void deleteImageFromHotel(Long hotelId, Long imageId, Long currentUserId) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("error.hotel.not.found"));

        if (!hotel.getManagerId().equals(currentUserId)) {
            throw new InvalidRequestException("error.hotel.image.unauthorized");
        }

        HotelImage hotelImage = hotelImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("error.hotel.image.not.found"));

        if (!hotelImage.getHotel().getId().equals(hotelId)) {
            throw new InvalidRequestException("error.hotel.image.mismatch");
        }
        hotelImageRepository.delete(hotelImage);
    }
}