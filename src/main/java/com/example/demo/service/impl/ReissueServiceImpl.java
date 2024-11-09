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

        String refreshToken = request.getHeader("refreshToken").split(" ")[1];

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

        String email = jwtTokenProvider.getEmail(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        String newAccessToken = jwtTokenProvider.createJwt(email, role, 600000L);
        String newRefreshToken = jwtTokenProvider.createJwt(email, role, 864000000L);

        System.out.println("newAccessToken: " + newAccessToken);
        System.out.println("newRefreshToken: " + newRefreshToken);

        // 기존 refreshToken 제거
        refreshTokenRepository.deleteByRefreshToken(refreshToken);


        response.setHeader("Authorization", newAccessToken);
        response.setHeader("refreshToken", newRefreshToken);

        new ResponseEntity<>(HttpStatus.OK);
    }
}
