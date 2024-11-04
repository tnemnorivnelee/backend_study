package com.example.demo.repository;

import com.example.demo.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Boolean existsByRefreshToken(String refreshToken);

    Boolean existsByUsername(String username);

    RefreshToken findByUsername(String username);

    @Transactional
    void deleteByRefreshToken(String refreshToken);
}
