package com.example.demo.controller;

import com.example.demo.dto.userDto.UpdateUserRoleRequest;
import com.example.demo.dto.userDto.UserRequestDTO;
import com.example.demo.dto.userDto.UpdateUserPasswordRequest;
import com.example.demo.service.impl.UserServiceImpl;
import com.example.demo.service.inter.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name="User Controller", description = "User 컨트롤러 부분")
public class UserController {

    private final UserService userService;

    // Create
    @PostMapping("/join")
    public ResponseEntity<String> addUser(@RequestBody UserRequestDTO request) {
        userService.save(request);

        return ResponseEntity.ok().body("Join Success : " + request.getEmail());
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
    public ResponseEntity<String> adminPage() {
        return ResponseEntity.ok("ROLE_ADMIN accessible page");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/role")
    public ResponseEntity<String> updateUserRole(@RequestBody UpdateUserRoleRequest request) {
        userService.updateRole(request);

        return ResponseEntity.ok("updateUserRole successful");
    }

    // Update pwd
    @PutMapping("/update")
    @Operation(summary = "update user password", description = "user 비밀번호를 변경합니다.")
    public ResponseEntity<String> updateUserPassword(@RequestBody UpdateUserPasswordRequest request) {
        userService.updatePassword(request);

        return ResponseEntity.ok("updatePassword and logout successful");
    }

    // Delete
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser() {
        userService.delete();

        return ResponseEntity.ok("user delete successful");
    }
}

