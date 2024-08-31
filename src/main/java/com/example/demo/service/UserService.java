package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.dto.AddUserRequest;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // Create
    public User save(AddUserRequest request) {

        if(!userRepository.existsByUserId(request.getUserId())) {
            return userRepository.save(request.toEntity());
        } else {
            return null;
        }
    }

    // Delete
    public void delete(String userId) {
        userRepository.deleteById(userId);
    }
}