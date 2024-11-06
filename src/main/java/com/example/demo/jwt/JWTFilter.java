package com.example.demo.jwt;

import com.example.demo.dto.userDto.CustomUserDetails;
import com.example.demo.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    // OncePerRequestFilter ??????????????????????????????????????????

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String authorization = request.getHeader("Authorization");
//        System.out.println("doFilterInternal auth : " + authorization);
//
//        if(authorization == null || !authorization.startsWith("Bearer ")) {
//            System.out.println("token null");
//            filterChain.doFilter(request, response); // ????????????????????????????????????????????
//
//            return;
//        }
//
//        System.out.println("authorization now");
//
//        String token = authorization.split(" ")[1]; // Bearer 제거
//
//        if (jwtTokenProvider.isExpired(token)) {
//            System.out.println("token expired");
//            filterChain.doFilter(request, response);
//
//            return;
//        }
//
//        String username = jwtTokenProvider.getUsername(token);
//        String role = jwtTokenProvider.getRole(token);
//
////        User user = new User();
////        user.setUsername(username);
////        user.setPassword("temppassword");
////        user.setRole(role);
//
//        User user = User.builder().username(username).password("temppassword").role(role).build();
//
//        System.out.println("JWTFilter : " + username + " " + role);
//
//        CustomUserDetails customUserDetails = new CustomUserDetails(user);
//
//        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//
//        filterChain.doFilter(request, response);
//    }

        // 헤더에서 access키에 담긴 토큰을 꺼냄
//        String accessToken = request.getHeader("Authorization").split(" ")[1];
        String accessToken = request.getHeader("Authorization");

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {

            filterChain.doFilter(request, response);
            // filterChain.doFilter()???????????????????????????????

            return;
        }

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtTokenProvider.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtTokenProvider.getCategory(accessToken);

        if (!category.equals("accessToken")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // username, role 값을 획득
        String username = jwtTokenProvider.getUsername(accessToken);
        String role = jwtTokenProvider.getRole(accessToken);

        User user = User.builder().username(username).role(role).build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}

