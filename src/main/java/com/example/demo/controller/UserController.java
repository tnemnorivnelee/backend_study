package com.example.demo.controller;

import com.example.demo.dto.userDto.UserResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.dto.userDto.UserRequestDTO;
import com.example.demo.dto.userDto.UpdateUserRequest;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserServiceImpl userServiceImpl;

    // Create
    @PostMapping("/join")
    public ResponseEntity<String> addUser(@RequestBody UserRequestDTO request) {

        UserResponseDTO user = userServiceImpl.save(request);

        return ResponseEntity.ok().body("Join Success : " + user.getUsername());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(request.getUsername() + " Login successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok(" Logout successful");
    }


    @GetMapping("/admin")
    public ResponseEntity<String> adminP() {
        return ResponseEntity.ok("ROLE_ADMIN accessible page");
    }

    // Update pwd
    @PutMapping("/update")
    public ResponseEntity<String> updateUserPwd(@RequestBody UpdateUserRequest request, @RequestHeader("Authorization") String authorization) {
        userServiceImpl.update(request, authorization);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/logout"));

        System.out.println("update and logout successful");

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authorization) {

        userServiceImpl.delete(authorization);

        return ResponseEntity.noContent().build();
    }
}

