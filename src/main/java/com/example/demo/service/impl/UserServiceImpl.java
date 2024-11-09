package com.example.demo.service.impl;

import com.example.demo.dto.userDto.CustomUserDetails;
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
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // Create
    @Override
    public UserResponseDTO save(UserRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("email already exists");
        }

        User dtoToEntity = request.toEntity(bCryptPasswordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(dtoToEntity);

        return UserResponseDTO.builder()
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    // Update
    @Override
    public void updatePassword(UpdateUserRequest request) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) principal;

        String jsonEmail = request.toEntity().getEmail();
        String tokenEmail = customUserDetails.getUsername();

        System.out.println("SecurityContextHolder info : " + tokenEmail + " " + customUserDetails.getPassword());

        // json email 과 SecurityContextHolder email 같은지 검사
        if (!tokenEmail.equals(jsonEmail)) {
            throw new NoSuchElementException("email mismatch");
        }

        // DB에서 저장된 유저 객체 받아오기
        User user = userRepository.findByEmail(tokenEmail)
                .orElseThrow(() -> new NoSuchElementException("username not found"));

        // update password
        user.update(bCryptPasswordEncoder.encode(request.getPassword()));

        // 로그아웃 하면서 토큰 모두 삭제
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/logout"));

        System.out.println("update and logout successful");
    }

    // Delete
    @Override
    public void delete() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) principal;

        String tokenEmail = customUserDetails.getUsername();

        // Find user
        if (!userRepository.existsByUsername(tokenEmail)) {
            throw notFountUser(tokenEmail);
        }

        // Delete user
        userRepository.deleteByEmail(tokenEmail);

        // 로그아웃 하면서 토큰 모두 삭제
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/logout"));

        System.out.println("user delete and logout successful");
    }

    private NoSuchElementException notFountUser(String username) {
        return new NoSuchElementException("no exist : " + username);
    }
}
