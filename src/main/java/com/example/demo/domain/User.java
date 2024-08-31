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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "pwd", nullable = false)
    private String pwd;

    @Column(name = "name", nullable = false)
    private String name;


    @Builder
    public User(String pwd, String name) {
        this.pwd = pwd;
        this.name = name;
    }

    public void update(String pwd) {
        this.pwd = pwd;
    }
}
