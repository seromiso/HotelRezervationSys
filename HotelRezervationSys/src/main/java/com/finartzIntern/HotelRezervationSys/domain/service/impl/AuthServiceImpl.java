package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.config.JwtService;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.LoginRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.RegisterRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.AuthResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserRole;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserStatus;
import com.finartzIntern.HotelRezervationSys.domain.repository.UserRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.AuthService;
import com.finartzIntern.HotelRezervationSys.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Override
    public AuthResponseDto register(RegisterRequestDto request){

        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new RuntimeException("Bu e-posta adresi zaten kullanımda!");
        }
        if (request.birthDate() == null) {
            throw new RuntimeException("Doğum tarihi boş bırakılamaz!");
        }

        // Doğum tarihi ile şu anki tarih arasındaki farkı hesaplıyoruz
        int age = Period.between(request.birthDate(), LocalDate.now()).getYears();

        if (age < 18) {
            throw new RuntimeException("18 yaşından küçük kullanıcılar sisteme kayıt olamaz !");
        }

        User user = new User();
        user.setName(request.name());
        user.setSurname(request.surname());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phone());
        user.setDateBirth(request.birthDate());
        user.setStatus(UserStatus.PASSIVE);
        user.setRole(UserRole.CUSTOMER);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);
        String verificationLink = "http://localhost:8080/api/v1/auth/verify?email=" + savedUser.getEmail();

        // 3. Mail gönderme metodu
        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getName(), verificationLink);


        String jwtToken = jwtService.generateToken(savedUser);
        return new AuthResponseDto(jwtToken, savedUser.getEmail(), savedUser.getRole());

    }

    @Override
    public AuthResponseDto login(LoginRequestDto request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

         //Kullanıcı için yeni bir JWT üret
        String jwtToken = jwtService.generateToken(user);

        // Token cevabını dön
        return new AuthResponseDto(jwtToken, user.getEmail(), user.getRole());
        }

        @Transactional
        public void verifyEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        user.setEmailVerified(true);       // E-postayı true yapıyoruz
        user.setStatus(UserStatus.ACTIVE); // Statüyü PASSIVE'den ACTIVE'e çekiyoruz

        userRepository.save(user);
      }


}
