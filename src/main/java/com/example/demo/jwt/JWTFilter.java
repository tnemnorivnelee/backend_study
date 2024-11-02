package com.example.demo.jwt;

import com.example.demo.dto.userDto.CustomUserDetails;
import com.example.demo.entity.User;
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

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    // OncePerRequestFilter ??????????????????????????????????????????

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        System.out.println("doFilterInternal auth : " + authorization);

        if(authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response); // ????????????????????????????????????????????

            return;
        }

        System.out.println("authorization now");

        String token = authorization.split(" ")[1]; // Bearer 제거

        if (jwtTokenProvider.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);

            return;
        }

        String username = jwtTokenProvider.getUsername(token);
        String role = jwtTokenProvider.getRole(token);

//        User user = new User();
//        user.setUsername(username);
//        user.setPassword("temppassword");
//        user.setRole(role);

        User user = User.builder().username(username).password("temppassword").role(role).build();

        System.out.println("JWTFilter : " + username + " " + role);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
