package com.example.demo.service.inter;

import com.example.demo.dto.userDto.JoinUserDTO;
import com.example.demo.dto.userDto.UpdateUserRequest;
import com.example.demo.entity.User;

public interface UserService {

    User save(JoinUserDTO request);

    User update(String userId, UpdateUserRequest request);

    void delete(String userId);
}
