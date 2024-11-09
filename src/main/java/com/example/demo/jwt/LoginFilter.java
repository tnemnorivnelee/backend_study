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

    public static final Long AT_EXPIRED_MS = 600000L;
    public static final Long RT_EXPIRED_MS = 864000000L;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // post /login 시도시,
    // 1. UsernamePasswordAuthenticationFilter
    // 2. Authentication Manager
    // 3. UserDetails
    // 4. UserDetailsService
    // 5. User Entity
    // 6. User Repository
    // 7. DB

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // request 를 받아 실제 인증을 거치는 authenticate 으로 전달

        // json 방식
        LoginDTO loginDTO;

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            ServletInputStream inputStream = request.getInputStream();

            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

            loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        System.out.println(email + " " + password);

        // DTO(UsernamePasswordAuthenticationToken)로 변환
        // role 넣는 법?????????????????????????????????????????????????
        // 로그인 하는 경우는 권한은 필요 없나?
        // UsernamePasswordAuthenticationToken 는 권한 넣는 메서드랑 안넣는 메서드 두개??
        // 안넣는 메서드는 권한 설정이 false 인데?
        // loginDTO 에 role 추가해서 넣어줘야 하나?
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        return authenticationManager.authenticate(authToken);
    }

    // 성공 시 실행 메서드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtTokenProvider.createJwt(email, role, AT_EXPIRED_MS);
        String refreshToken = jwtTokenProvider.createJwt(email, role, RT_EXPIRED_MS);

        System.out.println("accessToken : " + accessToken);
        System.out.println("refreshToken : " + refreshToken);

        response.setHeader("Authorization","Bearer " + accessToken);
        response.setHeader("refreshToken","Bearer " + refreshToken);
        response.setStatus(HttpStatus.OK.value()); // 200
        response.getWriter().write("login success : " + email);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
        System.out.println("Unsuccessful Authentication");
    }
}
