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
import io.jsonwebtoken.ExpiredJwtException;
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
    public void update(UpdateUserRequest request, String authorization) {

        User dtoToEntity = request.toEntity();
        String accessToken = checkAccessToken(authorization.split(" ")[1]);
        String tokenUsername = jwtTokenProvider.getUsername(accessToken);

        // 수정 요청한 username 과 토큰 username이 같은지 검사
        if (!tokenUsername.equals(dtoToEntity.getUsername())) {
            throw new NoSuchElementException("username mismatch");
        }

        User user = userRepository.findByUsername(tokenUsername);

        if(user == null) {
            throw new NoSuchElementException("User not found");
        }

        System.out.println(user.getUsername());

        // 비밀번호를 변경하면 access, refresh 토큰도 재발급?

        // 토큰 재발급
        String username = user.getUsername();
        String role = user.getRole();

        // refresh token delete
        deleteRefreshToken(username);

        // update password
        user.update(bCryptPasswordEncoder.encode(dtoToEntity.getPassword()));
    }

    // Delete
    @Override
    public void delete(String authorization) {
        String accessToken = checkAccessToken(authorization.split(" ")[1]);
        String username = jwtTokenProvider.getUsername(accessToken);

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

    private String checkAccessToken(String authorization) {
        // 1. access 토큰 가져오기
//        String accessToken = request.getHeader("Authorization");

        System.out.println("accessToken : " + authorization);

        if (authorization == null) {
            throw new NoSuchElementException("login info not found");
        }

        // 2. access 토큰이 만료되었나?
        try {
            jwtTokenProvider.isExpired(authorization);
        } catch (ExpiredJwtException e) {
            throw new NoSuchElementException("accessToken expired");
        }
        return authorization;
    }

    private NoSuchElementException notFountUser(String username) {
        return new NoSuchElementException("no exist : " + username);
    }
}