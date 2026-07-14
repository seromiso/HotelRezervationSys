package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.UserCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.UserResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/by-email")
    public ResponseEntity<UserResponseDto> getUserByEmail(@RequestParam String email) {
        UserResponseDto response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateRequestDto userRequestDto) {
        // Kullanıcı başarıyla oluşturulduğunda REST standartlarına göre 201 (CREATED) dönmek en iyisidir.
        UserResponseDto response = userService.createUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

}
