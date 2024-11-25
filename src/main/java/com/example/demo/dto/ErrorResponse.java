package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ErrorResponse {
    private final int statusCode;
    private final String message;
    private final String details;

    @Builder
    public ErrorResponse(int statusCode, String message, String details) {
        this.statusCode = statusCode;
        this.message = message;
        this.details = details;
    }
}
