package com.example.demo.controller;

import com.example.demo.dto.userDto.UserResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.dto.userDto.UserRequestDTO;
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

    @PostMapping("/join")
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody UserRequestDTO request) {

        UserResponseDTO user = userServiceImpl.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(request.getUsername() + " Login successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(request.getUsername() + " Logout successful");
    }


    @GetMapping("/admin")
    public ResponseEntity<String> adminP() {
        return ResponseEntity.ok("ROLE_ADMIN accessible page");
    }

//    // Update pwd
//    @PutMapping("/join/{username}")
//    public ResponseEntity<User> updateUserPwd(@PathVariable String username, @RequestBody UpdateUserRequest request) {
//        User updatedUserPwd = userServiceImpl.update(username, request);
//
//        return ResponseEntity.ok().body(updatedUserPwd);
//    }
//
//    // Delete
//    @DeleteMapping("/join/{username}")
//    public ResponseEntity<String> deleteUser(@PathVariable String username) {
//
//        userServiceImpl.delete(username);
//
//        return ResponseEntity.noContent().build();
//    }
}

