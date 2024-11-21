package com.example.demo.jwt;

import com.example.demo.common.Role;
import com.example.demo.dto.userDto.CustomUserDetails;
import com.example.demo.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    // OncePerRequestFilter ??????????????????????????????????????????

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String authorization = request.getHeader("Authorization");

        System.out.println("jwtFilter accessToken : " + authorization);


        // 토큰이 없다면 다음 필터로 넘김
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorization.split(" ")[1];

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        if (!jwtTokenProvider.validateToken(accessToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return;
        }

        // email, role 값을 획득
        String email = jwtTokenProvider.getEmail(accessToken);
        String role = jwtTokenProvider.getRole(accessToken);

        User user = User.builder().email(email).role(Role.valueOf(role)).build();

        // DTO 변환
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}

