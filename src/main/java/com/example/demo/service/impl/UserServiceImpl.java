package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.dto.userDto.AddUserRequest;
import com.example.demo.dto.userDto.UpdateUserRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.inter.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // Create
    @Override
    public User save(AddUserRequest request) {
        // 중복 검사 후 회원가입 진행

        if (userRepository.existsById(request.getUserId())) {
            throw new IllegalArgumentException("User already exists");
        }
        return userRepository.save(request.toEntity());
    }

    // Update
    @Override
    public User update(String userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> notFountUser(userId));

        user.update(request.getPwd());

        return user;
    }

    // Delete
    @Override
    public void delete(String userId) {
        if (!userRepository.existsById(userId)) {
            throw notFountUser(userId);
        }
        userRepository.deleteById(userId);
    }
    private NoSuchElementException notFountUser(String userId) {
        return new NoSuchElementException("no exist : " + userId);
    }
}