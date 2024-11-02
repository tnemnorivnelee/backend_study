package com.example.demo.jwt;

import com.example.demo.dto.userDto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // request 를 받아 실제 인증을 거치는 authenticate 으로 전달

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println(username);

        // DTO(UsernamePasswordAuthenticationToken)로 변환
        // role 넣는 법?????????????????????????????????????????????????
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    // 성공 시 실행 메서드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal(); // get 사용자 객체
        String username = customUserDetails.getUsername();

//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//
//        String role = auth.getAuthority();

        String role = authentication.getAuthorities().iterator().next().getAuthority();
        System.out.println("successful auth : " + username + " " + role);

        String token = jwtTokenProvider.createJwt(username, role, 60*60*1000L);
        System.out.println("after createjwt : " + jwtTokenProvider.getRole(token));;

        // Bearer 토큰은 OAuth 프레임워크에서 엑세스 토큰으로 사용하는 토큰 유형
        // Bearer == 소유자
        response.addHeader("Authorization", "Bearer " + token);

        System.out.println("Successful Authentication");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
        System.out.println("Unsuccessful Authentication");
    }
}
