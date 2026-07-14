package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.mappers.UserMapper;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.UserCreateRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.UserResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserStatus;
import com.finartzIntern.HotelRezervationSys.domain.repository.UserRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;



    @Override
    @Transactional(readOnly = true) // Sadece okuma yapacağı için performansı artırır
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı! ID: " + id));

        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Bu e-posta adresine ait kullanıcı bulunamadı: " + email));

        return userMapper.toResponseDto(user);
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
        user.setStatus(UserStatus.ACTIVE); // <-- Status enum ismine göre düzelt!

        User savedUser = userRepository.save(user);

        UserResponseDto response = new UserResponseDto();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setSurname(savedUser.getSurname());
        response.setEmail(savedUser.getEmail());
        response.setCreatedAt(savedUser.getCreatedAt());
        response.setStatus(savedUser.getStatus());
        response.setUpdatedAt(savedUser.getUpdatedAt());


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


}





