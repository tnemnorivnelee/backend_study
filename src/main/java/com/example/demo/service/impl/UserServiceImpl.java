package com.example.demo.service.impl;

import com.example.demo.enums.ErrorCode;
import com.example.demo.enums.Role;
import com.example.demo.dto.userDto.CustomUserDetails;
import com.example.demo.dto.userDto.UpdateUserRoleRequest;
import com.example.demo.entity.User;
import com.example.demo.dto.userDto.UserRequestDTO;
import com.example.demo.dto.userDto.UpdateUserPasswordRequest;
import com.example.demo.exception.AlreadyExistsException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.inter.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // Create
    @Override
    public void save(UserRequestDTO request) {

        if (request.getUsername().equals("admin")) {
            throw new AlreadyExistsException("admin already exists", ErrorCode.DUPLICATE_RESOURCE);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("email already exists", ErrorCode.DUPLICATE_RESOURCE);
        }

        User dtoToEntity = request.toEntity(bCryptPasswordEncoder.encode(request.getPassword()));

        userRepository.save(dtoToEntity);
    }

    // Update
    @Override
    public void updatePassword(UpdateUserPasswordRequest request) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) principal;

        String jsonEmail = request.toEntity().getEmail();
        String tokenEmail = customUserDetails.getUsername();

        System.out.println("SecurityContextHolder info : " + tokenEmail + " " + customUserDetails.getPassword());

        // json email 과 SecurityContextHolder email 같은지 검사
        if (!tokenEmail.equals(jsonEmail)) {
            throw new NoSuchElementException("email mismatch");
        }

        // DB에서 저장된 유저 객체 받아오기
        User user = userRepository.findByEmail(tokenEmail)
                .orElseThrow(() -> new NoSuchElementException("username not found"));

        // updatePassword password
        user.updatePassword(bCryptPasswordEncoder.encode(request.getPassword()));

        // 로그아웃 하면서 토큰 모두 삭제
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/logout"));

        System.out.println("updatePassword and logout successful");
    }

    @Override
    public void updateRole(UpdateUserRoleRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) principal;

        String jsonEmail = request.toEntity().getEmail();
        Role tokenRole = Role.valueOf(customUserDetails.getAuthorities().iterator().next().getAuthority());

        // 로그인 한 사람이 관리자인지 체크
        if (!tokenRole.equals(Role.ROLE_ADMIN)) {
            throw new NoSuchElementException("no admin user");
        }

        // role 수정 할 user 객체 가져오기
        User user = userRepository.findByEmail(jsonEmail)
                .orElseThrow(() -> new NoSuchElementException("email not found"));

        // 사용자 권한 체크
        if (user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AlreadyExistsException("The role is already set to ROLE_ADMIN", ErrorCode.DUPLICATE_RESOURCE);
        }

        user.updateRole(request.getRole());
    }

    // Delete
    @Override
    public void delete() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) principal;

        String tokenEmail = customUserDetails.getUsername();

        // Find user
        if (!userRepository.existsByEmail(tokenEmail)) {
            throw notFountUser(tokenEmail);
        }

        // Delete user
        userRepository.deleteByEmail(tokenEmail);

        // 로그아웃 하면서 토큰 모두 삭제
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/logout"));

        System.out.println("user delete and logout successful");
    }



    private NoSuchElementException notFountUser(String username) {
        return new NoSuchElementException("no exist : " + username);
    }
}
