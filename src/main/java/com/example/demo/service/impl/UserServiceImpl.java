package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.dto.userDto.JoinUserDTO;
import com.example.demo.dto.userDto.UpdateUserRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.inter.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // Create
    @Override
    public User save(JoinUserDTO request) {

        if (userRepository.existsById(request.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }

        User dtoToEntity = request.toEntity(bCryptPasswordEncoder.encode(request.getPassword()), "ROLE_ADMIN");

        return userRepository.save(dtoToEntity);
    }

    // Update
    @Override
    public User update(String username, UpdateUserRequest request) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> notFountUser(username));

        user.update(request.getPwd());

        return user;
    }

    // Delete
    @Override
    public void delete(String username) {
        if (!userRepository.existsById(username)) {
            throw notFountUser(username);
        }
        userRepository.deleteById(username);
    }
    private NoSuchElementException notFountUser(String username) {
        return new NoSuchElementException("no exist : " + username);
    }
}