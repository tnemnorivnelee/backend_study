package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.dto.AddUserRequest;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    // Create
    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody AddUserRequest request) {

        User user = userService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // Delete
    @DeleteMapping("/register/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {

        userService.delete(userId);

        return ResponseEntity.noContent().build();
    }
}

