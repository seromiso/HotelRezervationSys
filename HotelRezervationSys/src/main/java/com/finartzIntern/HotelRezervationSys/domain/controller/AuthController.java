package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.LoginRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RegisterRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.AuthResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto request){
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email) {
        authService.verifyEmail(email);
        return ResponseEntity.ok("E-posta adresiniz başarıyla doğrulandı! Rezervasyon için hazırsınız.");
    }
}
