package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckUserRepository extends JpaRepository<User, String> {

    boolean existsByUserId(String userId);
}
