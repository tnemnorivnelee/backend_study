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

        if (user == null) {
            // FORBIDDEN(403), 해당 요청에 대한 자원이 존재하므로 요청을 거부
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 
        } else {
            // CREATED(201), 새로운 자원을 성공적으로 생성
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
    }

    // Delete
    @DeleteMapping("/register/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.delete(userId);

        return ResponseEntity.noContent().build();
    }
}

