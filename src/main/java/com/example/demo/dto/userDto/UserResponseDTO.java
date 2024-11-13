package com.example.demo.dto.userDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDTO {

    private final String username;
    private final String email;
    private final String password;
    private final String role;

    @Builder
    public UserResponseDTO(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
