package com.example.demo.dto.userDto;

import com.example.demo.common.Role;
import com.example.demo.entity.User;
import lombok.Getter;

@Getter
public class UpdateUserRoleRequest {
    private String email;
    private Role role;

    public User toEntity() {
        // 빌더를 이용하여 dto를 entity로 만들어주는 메소드
        return User.builder()
                .email(email)
                .role(role)
                .build();
    }
}
