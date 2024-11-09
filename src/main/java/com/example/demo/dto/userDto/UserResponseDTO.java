package com.example.demo.dto.userDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDTO {

    private String username;
    private String email;
    private String password;
    private String role;

    @Builder
    public UserResponseDTO(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
