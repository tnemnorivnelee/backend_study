package com.example.demo.service;

import com.example.demo.domain.Article;
import com.example.demo.domain.User;
import com.example.demo.dto.AddUserRequest;
import com.example.demo.dto.UpdateArticleRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // Create
    public User save(AddUserRequest request) {
        // 중복 검사 후 회원가입 진행

        if(userRepository.existsById(request.getUserId())) {
            throw new IllegalArgumentException("User already exists");
        }
        return userRepository.save(request.toEntity());

//        return (User) userRepository.findById(request.getUserId())
//                .map(user -> {
//                    throw new IllegalArgumentException("User already exists");
//                }).orElseGet(() -> userRepository.save(request.toEntity()));


//        Optional<User> registeredUserId = userRepository.findById(request.getUserId());
//
//        if(registeredUserId.isEmpty()) {
//            return userRepository.save(request.toEntity());
//        } else {
//            throw new IllegalArgumentException("User already exists");
//        }
    }

    // Update
    @Transactional
    public User update(String userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("no exist : " + userId));

        user.update(request.getPwd());

        return user;
    }

    // Delete
    public void delete(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User does not exist");
        }
        userRepository.deleteById(userId);
    }
}