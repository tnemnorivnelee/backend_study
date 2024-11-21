package com.example.demo.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //path and method verify
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        //get access token
        String accessToken = request.getHeader("Authorization").split(" ")[1];

        //access null check
        if (accessToken == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("accessToken : " + accessToken);
            return;
        }

        //expired check
        if (!jwtTokenProvider.validateToken(accessToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return;
        }

        //로그아웃 진행

        // 모든 토큰 제거
        request.setAttribute("Authorization", null);
        request.setAttribute("refreshToken", null);

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("logout success");
    }
}
