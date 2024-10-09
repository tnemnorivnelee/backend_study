package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.dto.userDto.JoinUserDTO;
import com.example.demo.dto.userDto.UpdateUserRequest;
import com.example.demo.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserServiceImpl userServiceImpl;

    // Create
    @PostMapping("/join")
    public ResponseEntity<User> addUser(@RequestBody JoinUserDTO request) {

        User user = userServiceImpl.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // Update pwd
    @PutMapping("/join/{username}")
    public ResponseEntity<User> updateUserPwd(@PathVariable String username, @RequestBody UpdateUserRequest request) {
        User updatedUserPwd = userServiceImpl.update(username, request);

        return ResponseEntity.ok().body(updatedUserPwd);
    }

    // Delete
    @DeleteMapping("/join/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {

        userServiceImpl.delete(username);

        return ResponseEntity.noContent().build();
    }
}

