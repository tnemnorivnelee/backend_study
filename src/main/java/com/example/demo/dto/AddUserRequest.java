package com.example.demo.dto;

import com.example.demo.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddUserRequest {

    private String pwd;
    private String name;

    public User toEntity() {
        return User.builder()
                .pwd(pwd)
                .name(name)
                .build();
    }
}