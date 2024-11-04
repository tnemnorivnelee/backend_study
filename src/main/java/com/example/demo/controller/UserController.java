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
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody UserRequestDTO request) {

        UserResponseDTO user = userServiceImpl.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
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
    public ResponseEntity<String> updateUserPwd(@RequestBody UpdateUserRequest request) {
        userServiceImpl.update(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/logout"));

        System.out.println("update and logout successful");

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {

        userServiceImpl.delete(username);

        return ResponseEntity.noContent().build();
    }
}

