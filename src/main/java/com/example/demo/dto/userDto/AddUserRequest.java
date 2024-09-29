package com.example.demo.dto.userDto;

import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddUserRequest {

    private String userId;
    private String pwd;
    private String name;


    public User toEntity() {
        return User.builder()
                .userId(userId)
                .pwd(pwd)
                .name(name)
                .build();
    }
}