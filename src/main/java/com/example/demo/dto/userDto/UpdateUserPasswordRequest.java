package com.example.demo.dto.userDto;

import com.example.demo.entity.User;
import lombok.Getter;

@Getter
public class UpdateUserPasswordRequest {
    private String username;
    private String password;

    public User toEntity() {
        // 빌더를 이용하여 dto를 entity로 만들어주는 메소드
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }
}