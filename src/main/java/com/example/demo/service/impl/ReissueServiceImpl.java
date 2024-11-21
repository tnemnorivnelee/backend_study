package com.example.demo.service.impl;

import com.example.demo.common.Role;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.service.inter.ReissueService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class ReissueServiceImpl implements ReissueService {

//    public static final Long AT_EXPIRED_MS = 600000L;
    public static final Long AT_EXPIRED_MS = 60000L; // 임시

    public static final Long RT_EXPIRED_MS = 864000000L;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public void reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String token = request.getHeader("refreshToken");

        System.out.println("reissue RT : " + token);

        if (token == null || !token.startsWith("Bearer ")) {
            new ResponseEntity<>("refreshToken token null", HttpStatus.BAD_REQUEST);
            return;
        }

        String refreshToken = token.split(" ")[1];

        //expired check
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
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
