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
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReissueServiceImpl implements ReissueService {

    public static final Long AT_EXPIRED_MS = 600000L;
    public static final Long RT_EXPIRED_MS = 864000000L;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public void reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String token = request.getHeader("refreshToken");

        if (token == null || !token.startsWith("Bearer ")) {
            new ResponseEntity<>("refreshToken token null", HttpStatus.BAD_REQUEST);
            return;
        }

        String refreshToken = token.split(" ")[1];

        try {
            jwtTokenProvider.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            new ResponseEntity<>("refreshToken token expired", HttpStatus.BAD_REQUEST);
            return;
        }

        String email = jwtTokenProvider.getEmail(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        String newAccessToken = jwtTokenProvider.createJwt(email, role, AT_EXPIRED_MS);
        String newRefreshToken = jwtTokenProvider.createJwt(email, role, RT_EXPIRED_MS);

        System.out.println("newAccessToken: " + newAccessToken);
        System.out.println("newRefreshToken: " + newRefreshToken);

        response.setHeader("Authorization", newAccessToken);
        response.setHeader("refreshToken", newRefreshToken);

        response.setStatus(HttpStatus.OK.value()); // 200
        response.getWriter().write("reissue success : " + email);
    }
}
