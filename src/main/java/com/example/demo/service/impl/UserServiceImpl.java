package com.example.demo.service.impl;

import com.example.demo.dto.userDto.CustomUserDetails;
import com.example.demo.dto.userDto.UserResponseDTO;
import com.example.demo.entity.Article;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
//    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    // Create
    @Override
    public UserResponseDTO save(UserRequestDTO request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }

        String encodedPwd = bCryptPasswordEncoder.encode(request.getPassword());
        System.out.println("join encoded pwd : " +  encodedPwd);
        User dtoToEntity = request.toEntity(encodedPwd);

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

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) principal;

        System.out.println("SecurityContextHolder info : " + customUserDetails.getUsername() + " " + customUserDetails.getPassword());

        User dtoToEntity = request.toEntity(); // 수정할 값이 담긴 객체
        String tokenUsername = customUserDetails.getUsername(); // 토큰에서 추출한 username 정보

        User user = userRepository.findByUsername(tokenUsername);

        // 수정 요청한 username 과 토큰 username이 같은지 검사
        if (!tokenUsername.equals(dtoToEntity.getUsername())) {
            throw new NoSuchElementException("username mismatch");
        }

//        User user = userRepository.findByUsername(tokenUsername);
        UserDetails userDetails = userDetailsService.loadUserByUsername(tokenUsername);


        if(userDetails == null) {
            throw new NoSuchElementException("User not found");
        }

        System.out.println(userDetails.getUsername());

        // 비밀번호를 변경하면 access, refresh 토큰도 재발급?

        // 토큰 재발급
        String username = userDetails.getUsername();
//        String role = user.getRole();

        // refresh token delete
        deleteRefreshToken(username);

        // update password
        String encodedPwd = bCryptPasswordEncoder.encode(dtoToEntity.getPassword());
        System.out.println("join update pwd : " + encodedPwd);
        dtoToEntity.update(encodedPwd);
        // 유저 레포지토리에 바뀐 값 저장하는 로직 추가하기
    }

    // Delete
    @Override
    public void delete() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) principal;

        String tokenUsername = customUserDetails.getUsername();

        // Find user
        if (!userRepository.existsByUsername(tokenUsername)) {
            throw notFountUser(tokenUsername);
        }

        // logout 으로 날리는게 좋은가??
        deleteRefreshToken(tokenUsername);

        // Delete user
        userRepository.deleteByUsername(tokenUsername);
    }

    private void deleteRefreshToken(String username) {
        if(!refreshTokenRepository.existsByUsername(username)) {
            throw notFountUser(username);
        }
        String refreshToken = refreshTokenRepository.findByUsername(username).getRefreshToken();

        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }

//    private String checkAccessToken(String authorization) {
//        // 1. access 토큰 가져오기
////        String accessToken = request.getHeader("Authorization");
//
//        System.out.println("accessToken : " + authorization);
//
//        if (authorization == null) {
//            throw new NoSuchElementException("login info not found");
//        }
//
//        // 2. access 토큰이 만료되었나?
//        try {
//            jwtTokenProvider.isExpired(authorization);
//        } catch (ExpiredJwtException e) {
//            throw new NoSuchElementException("accessToken expired");
//        }
//        return authorization;
//    }

    private NoSuchElementException notFountUser(String username) {
        return new NoSuchElementException("no exist : " + username);
    }
}