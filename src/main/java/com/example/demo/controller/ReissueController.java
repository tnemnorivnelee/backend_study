package com.example.demo.controller;

import com.example.demo.entity.RefreshToken;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class ReissueController {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 나중에 ReissueServiceImpl 로 분리하기!!!!!!!!!!!!!!!!!!!!!!!!!!

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
            return new ResponseEntity<>("refreshToken token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtTokenProvider.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refreshToken token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtTokenProvider.getCategory(refreshToken);

        if (!category.equals("refreshToken")) {

            //response status code
            return new ResponseEntity<>("invalid refreshToken token", HttpStatus.BAD_REQUEST);
        }

        // DB 에 저장되어 있는지 확인
        if (!refreshTokenRepository.existsByRefreshToken(refreshToken)) {

            return new ResponseEntity<>("refreshToken does not exist", HttpStatus.BAD_REQUEST);
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
        addRefreshEntity(username, newRefreshToken, 86400000L);

        //response
        response.setHeader("accessToken", newAccessToken);
        response.addCookie(createCookie("refreshToken", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

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
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}