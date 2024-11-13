package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor //(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", updatable = false, nullable = false)
    private String username;

    @Column(name = "email", updatable = false, nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name ="role", nullable = false)
    private String role;

    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = "ROLE_USER";
        }
    }


    @Builder
    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRole(String role) {
        this.role = role;
    }
}
