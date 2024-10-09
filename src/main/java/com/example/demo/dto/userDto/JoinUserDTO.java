package com.example.demo.dto.userDto;

import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JoinUserDTO {

    private String username;
    private String password;
    private String name;
    private String role;


    public User toEntity(String password, String role) {
        return User.builder()
                .username(username)
                .password(password)
                .name(name)
                .role(role)
                .build();
    }
}