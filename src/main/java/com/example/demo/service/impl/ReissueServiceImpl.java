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

        //get refreshToken token
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refreshToken")) {

                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {

            //response status code
            new ResponseEntity<>("refreshToken token null", HttpStatus.BAD_REQUEST);
            return;
        }

        //expired check
        try {
            jwtTokenProvider.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {

            //response status code
            new ResponseEntity<>("refreshToken token expired", HttpStatus.BAD_REQUEST);
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtTokenProvider.getCategory(refreshToken);

        if (!category.equals("refreshToken")) {

            //response status code
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

        //make new JWT
        String newAccessToken = jwtTokenProvider.createJwt("accessToken", username, role, 600000L);
        String newRefreshToken = jwtTokenProvider.createJwt("refreshToken", username, role, 864000000L);

        System.out.println("newAccessToken: " + newAccessToken);
        System.out.println("newRefreshToken: " + newRefreshToken);

        // refresh 토큰 저장 DB에 기존 refresh 토큰 삭제 후 새 refresh 토큰을 저장
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
        addRefreshEntity(username, newRefreshToken);

        //response
        response.setHeader("Authorization", newAccessToken);
        response.addCookie(createCookie("refreshToken", newRefreshToken));

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

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
