package com.example.demo.jwt;

import com.example.demo.dto.userDto.LoginDTO;
import com.example.demo.entity.RefreshToken;
import com.example.demo.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // request 를 받아 실제 인증을 거치는 authenticate 으로 전달

//        // form-data 방식
//        String username = obtainUsername(request);
//        String password = obtainPassword(request);

//        System.out.println(username);

        // json 방식
        LoginDTO loginDTO = new LoginDTO();

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            ServletInputStream inputStream = request.getInputStream();

            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

            loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(loginDTO.getUsername());

        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();


        // DTO(UsernamePasswordAuthenticationToken)로 변환
        // role 넣는 법?????????????????????????????????????????????????
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    // 성공 시 실행 메서드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtTokenProvider.createJwt("accessToken", username, role, 600000L);
        String refreshToken = jwtTokenProvider.createJwt("refreshToken", username, role, 86400000L);

        System.out.println("accessToken : " + accessToken);
        System.out.println("refreshToken : " + refreshToken);

        addRefreshToken(username, refreshToken);

        response.setHeader("Authorization", accessToken);
        response.addCookie(createCookie("refreshToken", refreshToken));
        response.setStatus(HttpStatus.OK.value()); // 200

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
        System.out.println("Unsuccessful Authentication");
    }

    private void addRefreshToken(String username, String refresh) {

        Date date = new Date(System.currentTimeMillis() + 86400000L);

        RefreshToken refreshToken = RefreshToken.builder()
                .username(username)
                .refreshToken(refresh)
                .expiration(date.toString())
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60); // 쿠키 생명 주기
//        cookie.setSecure(true); // https 통신을 진행하는 경우 사용
//        cookie.setPath("/"); // 쿠키가 적용될 범위
        cookie.setHttpOnly(true); // 자바스크립트로 해당 쿠키를 접근하지 못하도록 설정

        return cookie;
    }
}
