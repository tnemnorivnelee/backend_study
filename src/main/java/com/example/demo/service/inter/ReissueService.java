package com.example.demo.service.inter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ReissueService {

    void reissue(HttpServletRequest request, HttpServletResponse response);

}
