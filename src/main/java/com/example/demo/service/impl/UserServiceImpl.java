package com.example.demo.service.impl;

import com.example.demo.dto.userDto.UserResponseDTO;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.dto.userDto.UserRequestDTO;
import com.example.demo.dto.userDto.UpdateUserRequest;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.inter.UserService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // Create
    @Override
    public UserResponseDTO save(UserRequestDTO request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }

        User dtoToEntity = request.toEntity(bCryptPasswordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(dtoToEntity);

        return UserResponseDTO.builder()
                .username(savedUser.getUsername())
//                .password(savedUser.getPassword()) // 비밀번호를 굳이 유출?
                .role(savedUser.getRole())
                .build();
    }

    // Update
    @Override
    public void update(UpdateUserRequest request) {

        User dtoToEntity = request.toEntity();

        User user = userRepository.findByUsername(dtoToEntity.getUsername());

        if(user == null) {
            throw new NoSuchElementException("User not found");
        }

        System.out.println(user.getUsername());

        // 비밀번호를 변경하면 access, refresh 토큰도 재발급?

        // 토큰 재발급
        String username = user.getUsername();
        String role = user.getRole();

        String newAccessToken = jwtTokenProvider.createJwt("accessToken", username, role, 600000L);
        String newRefreshToken = jwtTokenProvider.createJwt("refreshToken", username, role, 864000000L);

        // refresh token delete
        deleteRefreshToken(username);

        // update password
        user.update(bCryptPasswordEncoder.encode(dtoToEntity.getPassword()));
    }

    // Delete
    @Override
    public void delete(String username) {
        // Find user
        if (!userRepository.existsByUsername(username)) {
            throw notFountUser(username);
        }

        deleteRefreshToken(username);

        User user = userRepository.findByUsername(username);
        Long userId = (long) user.getId();

        // Delete user
        userRepository.deleteById(String.valueOf(userId));
    }

    private void deleteRefreshToken(String username) {
        if(!refreshTokenRepository.existsByUsername(username)) {
            throw notFountUser(username);
        }
        String refreshToken = refreshTokenRepository.findByUsername(username).getRefreshToken();

        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }

    private NoSuchElementException notFountUser(String username) {
        return new NoSuchElementException("no exist : " + username);
    }
}