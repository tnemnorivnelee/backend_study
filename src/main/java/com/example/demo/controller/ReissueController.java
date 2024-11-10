package com.example.demo.controller;

import com.example.demo.service.inter.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ReissueController {
    private final ReissueService reissueService;


    @PostMapping("/reissue")
    public void reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        reissueService.reissue(request, response);
    }
}