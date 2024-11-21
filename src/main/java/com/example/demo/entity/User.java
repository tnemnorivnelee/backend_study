package com.example.demo.entity;

import com.example.demo.common.Role;
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
    @Enumerated(EnumType.STRING)
    private Role role;

    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = Role.ROLE_USER;
        }
    }


    @Builder
    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRole(Role role) {
        this.role = role;
    }
}
