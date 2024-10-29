package com.example.demo.dto.userDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDTO {

    private String username;
    private String password;
    private String role;

    @Builder
    public UserResponseDTO(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
