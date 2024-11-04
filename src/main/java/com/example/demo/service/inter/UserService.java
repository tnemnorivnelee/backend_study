package com.example.demo.service.inter;

import com.example.demo.dto.userDto.UserRequestDTO;
import com.example.demo.dto.userDto.UpdateUserRequest;
import com.example.demo.dto.userDto.UserResponseDTO;
import com.example.demo.entity.User;

public interface UserService {

    UserResponseDTO save(UserRequestDTO request);

    void update(UpdateUserRequest request);

    void delete(String userId);
}
