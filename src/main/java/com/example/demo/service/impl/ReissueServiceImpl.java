package com.example.demo.service.impl;

import com.example.demo.entity.RefreshToken;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.service.inter.ReissueService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReissueServiceImpl implements ReissueService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void reissue(HttpServletRequest request, HttpServletResponse response) {

        // 쿠키에서 refreshToken 가져오기
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            new ResponseEntity<>("refreshToken token null", HttpStatus.BAD_REQUEST);
            return;
        }

        try {
            jwtTokenProvider.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            new ResponseEntity<>("refreshToken token expired", HttpStatus.BAD_REQUEST);
            return;
        }

        // refreshToken 확인
        String category = jwtTokenProvider.getCategory(refreshToken);

        if (!category.equals("refreshToken")) {
            new ResponseEntity<>("invalid refreshToken token", HttpStatus.BAD_REQUEST);
            return;
        }

        // DB 에 저장되어 있는지 확인
        if (!refreshTokenRepository.existsByRefreshToken(refreshToken)) {
            new ResponseEntity<>("refreshToken does not exist", HttpStatus.BAD_REQUEST);
            return;
        }

        String username = jwtTokenProvider.getUsername(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        String newAccessToken = jwtTokenProvider.createJwt("accessToken", username, role, 600000L);
        String newRefreshToken = jwtTokenProvider.createJwt("refreshToken", username, role, 864000000L);

        System.out.println("newAccessToken: " + newAccessToken);
        System.out.println("newRefreshToken: " + newRefreshToken);

        // 기존 refreshToken 제거
        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        // 새 refresh 토큰 저장
        addRefreshEntity(username, newRefreshToken);

        response.setHeader("Authorization", newAccessToken);
        response.setHeader("refreshToken", newRefreshToken);

        new ResponseEntity<>(HttpStatus.OK);
    }

    private void addRefreshEntity(String username, String refresh) {

        Date date = new Date(System.currentTimeMillis() + 86400000L);

        RefreshToken refreshToken = RefreshToken.builder()
                .username(username)
                .refreshToken(refresh)
                .expiration(date.toString())
                .build();

        refreshTokenRepository.save(refreshToken);
    }
}
