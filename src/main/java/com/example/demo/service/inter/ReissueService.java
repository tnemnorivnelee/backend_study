package com.example.demo.service.inter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ReissueService {

    void reissue(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
