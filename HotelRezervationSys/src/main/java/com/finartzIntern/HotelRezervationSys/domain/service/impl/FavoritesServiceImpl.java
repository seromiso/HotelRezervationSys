package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ConflictException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
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

        // Zaten favorilerde varsa 409 Conflict fırlatıyoruz
        if (favoritesRepository.existsByUserIdAndHotelId(requestDto.getUserId(), requestDto.getHotelId())) {
            throw new ConflictException("error.favorites.allready");
        }

        Favorites favorite = new Favorites();

        // Kullanıcı yoksa 404 Not Found fırlatıyoruz
        favorite.setUser(userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("error.user.not.found" + requestDto.getUserId())));

        // Otel yoksa 404 Not Found fırlatıyoruz
        favorite.setHotel(hotelRepository.findById(requestDto.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("error.hotel.not.found ID: " + requestDto.getHotelId())));

        Favorites savedFavorite = favoritesRepository.save(favorite);
        return convertToResponseDto(savedFavorite);
    }

    @Override
    public void removeFavorite(FavoriteRequestDto requestDto) {
        boolean exists = favoritesRepository.existsByUserIdAndHotelId(requestDto.getUserId(), requestDto.getHotelId());
        if (!exists) {
            throw new ResourceNotFoundException("error.favorites.no.favorites.found");
        }
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