package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.dto.AddUserRequest;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<User> addUser(@RequestBody AddUserRequest request) {

        User user = userService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
