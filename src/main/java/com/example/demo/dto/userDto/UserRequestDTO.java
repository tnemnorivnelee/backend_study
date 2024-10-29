package com.example.demo.dto.userDto;

import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserRequestDTO {

    private String username;
    private String password;
    private String role;


    public User toEntity(String password, String role) {
        return User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
    }
}