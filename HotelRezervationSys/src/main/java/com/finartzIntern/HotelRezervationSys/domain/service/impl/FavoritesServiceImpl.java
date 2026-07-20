package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.Favorites;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.FavoriteRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.FavoriteResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.repository.FavoritesRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.UserRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.FavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoritesServiceImpl implements FavoritesService {

    private final FavoritesRepository favoritesRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;

    @Override
    public FavoriteResponseDto addFavorite(FavoriteRequestDto requestDto) {
        if (favoritesRepository.existsByUserIdAndHotelId(requestDto.getUserId(), requestDto.getHotelId()))
            throw new RuntimeException("Bu otel zaten favorilerinizde!");

        Favorites favorite = new Favorites();
        favorite.setUser(userRepository.findById(requestDto.getUserId()).orElseThrow());
        favorite.setHotel(hotelRepository.findById(requestDto.getHotelId()).orElseThrow());

        Favorites savedFavorite = favoritesRepository.save(favorite);
        return convertToResponseDto(savedFavorite);
    }

    @Override
    public void removeFavorite(FavoriteRequestDto requestDto) {
        favoritesRepository.deleteByUserIdAndHotelId(requestDto.getUserId(), requestDto.getHotelId());
    }

    @Override
    public List<FavoriteResponseDto> getUserFavorites(Long userId) {
        List<Favorites> favoritesList = favoritesRepository.findByUserId(userId);
        return favoritesList.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    private FavoriteResponseDto convertToResponseDto(Favorites favorite) {
        return FavoriteResponseDto.builder()
                .id(favorite.getId())
                .userId(favorite.getUser().getId())
                .hotelId(favorite.getHotel().getId())
                .hotelName(favorite.getHotel().getName()) //
                .build();
    }
}