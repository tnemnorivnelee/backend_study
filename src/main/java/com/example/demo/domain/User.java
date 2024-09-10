package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "user_id", updatable = false)
    private String userId;

    @Column(name = "pwd", nullable = false)
    private String pwd;

    @Column(name = "name", nullable = false)
    private String name;


    @Builder
    public User(String userId, String pwd, String name) {
        this.userId = userId;
        this.pwd = pwd;
        this.name = name;
    }

    public void update(String pwd) {
        this.pwd = pwd;
    }
}
