package com.example.demo.controller;

import com.example.demo.entity.RefreshToken;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.service.impl.ReissueServiceImpl;
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
    private final ReissueServiceImpl reissueServiceImpl;


    @PostMapping("/reissue")
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        reissueServiceImpl.reissue(request, response);
    }
}