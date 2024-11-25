package com.example.demo.dto.userDto;

import com.example.demo.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDTO {

    private final String username;
    private final String email;
    private final String password;
    private final Role role;

    @Builder
    public UserResponseDTO(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
