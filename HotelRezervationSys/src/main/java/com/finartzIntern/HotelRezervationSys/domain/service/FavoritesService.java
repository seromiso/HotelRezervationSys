package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.FavoriteRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.FavoriteResponseDto;
import java.util.List;

public interface FavoritesService {
    FavoriteResponseDto addFavorite(FavoriteRequestDto requestDto);
    void removeFavorite(FavoriteRequestDto requestDto);
    List<FavoriteResponseDto> getUserFavorites(Long userId);
}
