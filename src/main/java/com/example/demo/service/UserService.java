package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.dto.AddUserRequest;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // Create
    public User save(AddUserRequest request) {

        Optional<User> registeredUserId = userRepository.findById(request.getUserId());

        if(registeredUserId.isEmpty()) {
            return userRepository.save(request.toEntity());
        } else {
            throw new IllegalArgumentException("User already exists");
        }
    }

    // Delete
    public void delete(String userId) {
        userRepository.deleteById(userId);
    }
}