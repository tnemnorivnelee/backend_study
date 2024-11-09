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
    private String email;
    private String password;
    private String role;


    public User toEntity(String password) {
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .build();
    }
}