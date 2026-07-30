package com.finartzIntern.HotelRezervationSys.domain.controller;

import com.finartzIntern.HotelRezervationSys.config.SecurityUtils;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.BadRequestException;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.LoginRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RegisterRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.AuthResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.service.AuthService;
import com.finartzIntern.HotelRezervationSys.domain.service.EmailService;
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
    public ResponseEntity<AuthResponseDto> verifyEmail(@RequestParam String email) {

        return ResponseEntity.ok(authService.verifyEmail(email));
    }
    @PostMapping("/resend-verification-email")
    public ResponseEntity<String> resendVerificationEmail() {
        authService.resendVerificationEmail();
        return ResponseEntity.ok("Doğrulama bağlantısı e-posta adresinize tekrar gönderildi.");
    }
}
