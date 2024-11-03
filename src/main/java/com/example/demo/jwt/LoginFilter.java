package com.example.demo.jwt;

import com.example.demo.entity.RefreshToken;
import com.example.demo.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
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

import java.util.Date;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // request 를 받아 실제 인증을 거치는 authenticate 으로 전달

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println(username);

        // DTO(UsernamePasswordAuthenticationToken)로 변환
        // role 넣는 법?????????????????????????????????????????????????
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    // 성공 시 실행 메서드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal(); // get 사용자 객체
//        String username = customUserDetails.getUsername();
//
////        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
////        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
////        GrantedAuthority auth = iterator.next();
////
////        String role = auth.getAuthority();
//
//        String role = authentication.getAuthorities().iterator().next().getAuthority();
//        System.out.println("successful auth : " + username + " " + role);
//
//        String token = jwtTokenProvider.createJwt(username, role, 60*60*1000L);
//        System.out.println("after createjwt : " + jwtTokenProvider.getRole(token));;
//
//        // Bearer 토큰은 OAuth 프레임워크에서 엑세스 토큰으로 사용하는 토큰 유형
//        // Bearer == 소유자
//        response.addHeader("Authorization", "Bearer " + token);
//
//        System.out.println("Successful Authentication");

        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtTokenProvider.createJwt("accessToken", username, role, 600000L);
        String refreshToken = jwtTokenProvider.createJwt("refreshToken", username, role, 86400000L);

        System.out.println("accessToken : " + accessToken);
        System.out.println("refreshToken : " + refreshToken);

        addRefreshToken(username, refreshToken, 86400000L);

        response.setHeader("accessToken", accessToken);
        response.addCookie(createCookie("refreshToken", refreshToken));
        response.setStatus(HttpStatus.OK.value()); // 200

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
        System.out.println("Unsuccessful Authentication");
    }

    private void addRefreshToken(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

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
