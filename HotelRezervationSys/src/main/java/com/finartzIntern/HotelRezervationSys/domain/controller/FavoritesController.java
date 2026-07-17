package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.FavoriteRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.FavoriteResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.FavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/favorites")
@RequiredArgsConstructor
public class FavoritesController {
    private final FavoritesService favoritesService;

    @PostMapping
    public ResponseEntity<FavoriteResponseDto> addFavorite(@RequestBody FavoriteRequestDto favoriteRequestDto) {
        FavoriteResponseDto response = favoritesService.addFavorite(favoriteRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@RequestBody FavoriteRequestDto favoriteRequestDto) {
        favoritesService.removeFavorite(favoriteRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteResponseDto>> getUserFavorites(@PathVariable Long userId) {
        List<FavoriteResponseDto> response = favoritesService.getUserFavorites(userId);
        return ResponseEntity.ok(response);
    }
}
