package com.finartzIntern.HotelRezervationSys.domain.service.impl;
import com.finartzIntern.HotelRezervationSys.config.SecurityConfig;
import com.finartzIntern.HotelRezervationSys.config.SecurityUtils;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.UserCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.UserUpdateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.UserProfileResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.UserResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserRole;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserStatus;
import com.finartzIntern.HotelRezervationSys.domain.repository.UserRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;





@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    private UserResponseDto mapToDto(User user){
        UserResponseDto dto = new UserResponseDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setBirthDate(user.getDateBirth());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı! ID: " + id));

    return mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Bu e-posta adresine ait kullanıcı bulunamadı: " + email));

        return mapToDto(user);


    }
    @Override
    public UserResponseDto createUser(UserCreateRequestDto userRequestDto) {

        User user = new User();
        user.setName(userRequestDto.getName());
        user.setSurname(userRequestDto.getSurname());
        user.setEmail(userRequestDto.getEmail());
        user.setPasswordHash(userRequestDto.getPassword());
        user.setRole(userRequestDto.getRole());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setDateBirth(userRequestDto.getDateBirth());
        user.setStatus(UserStatus.PASSIVE);

        User savedUser = userRepository.save(user);

        UserResponseDto response = new UserResponseDto();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setSurname(savedUser.getSurname());
        response.setEmail(savedUser.getEmail());
        response.setCreatedAt(savedUser.getCreatedAt());
        response.setStatus(savedUser.getStatus());
        response.setUpdatedAt(savedUser.getUpdatedAt());
        response.setEmail_verified(false);


        return response;
    }
    @Override
    public List<UserResponseDto> getAllUsers() {
        // 1. Veri tabanından tüm kullanıcıları çek
        List<User> users = userRepository.findAll();

        // 2. Entity listesini DTO listesine çevir (Mapping)
        return users.stream().map(user -> {
            UserResponseDto response = new UserResponseDto();
            response.setId(user.getId());
            response.setName(user.getName());
            response.setSurname(user.getSurname());
            response.setEmail(user.getEmail());
            response.setRole(user.getRole());
            response.setPhoneNumber(user.getPhoneNumber());
            response.setBirthDate(user.getDateBirth());

            response.setCreatedAt(user.getCreatedAt());
            response.setUpdatedAt(user.getUpdatedAt());
            response.setStatus(user.getStatus());

            return response;
        }).collect(Collectors.toList());
    }
    @Override
    public UserProfileResponseDto getUserProfile() {
        User currentUser = SecurityUtils.getCurrentUser();

        return new UserProfileResponseDto(
                currentUser.getId(),
                currentUser.getName(),
                currentUser.getSurname(),
                currentUser.getEmail(),
                currentUser.getPhoneNumber(),
                currentUser.getDateBirth(),
                currentUser.getRole(),
                currentUser.getStatus()
        );
    }
    @Override
    @Transactional
    public UserProfileResponseDto updateUserProfile(UserUpdateRequestDto dto){
        User user = userRepository.findById(SecurityUtils.getCurrentUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı!"));

        if(dto.name() != null && !dto.name().isBlank()){
            user.setName(dto.name());
        }
        if(dto.surname() != null && !dto.surname().isBlank()){
            user.setSurname(dto.surname());
        }
        if(dto.phone() != null && !dto.phone().isBlank()){
            user.setPhoneNumber(dto.phone());
        }
        if (dto.newPassword() != null && !dto.newPassword().isBlank()) {
            if (dto.currentPassword() == null || dto.currentPassword().isBlank()) {
                throw new RuntimeException("Şifrenizi değiştirmek için mevcut şifrenizi girmelisiniz!");
            }

            // Mevcut şifre veritabanındakiyle eşleşiyor mu kontrolü
            if (!passwordEncoder.matches(dto.currentPassword(), user.getPasswordHash())) {
                throw new RuntimeException("Mevcut şifreniz hatalı!");
            }

            // YENİ KONTROL: Yeni şifre, mevcut girilen şifre ile aynı olamaz!
            if (dto.currentPassword().equals(dto.newPassword())) {
                throw new RuntimeException("Yeni şifreniz mevcut şifreniz ile aynı olamaz!");
            }

            user.setPasswordHash(passwordEncoder.encode(dto.newPassword()));
        }
        User updatedUser = userRepository.save(user);
        return new UserProfileResponseDto(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getSurname(),
                updatedUser.getEmail(),
                updatedUser.getPhoneNumber(),
                updatedUser.getDateBirth(),
                updatedUser.getRole(),
                updatedUser.getStatus()
        );
    }


}





