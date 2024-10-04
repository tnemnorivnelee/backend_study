package com.example.demo.service.inter;

import com.example.demo.dto.userDto.AddUserRequest;
import com.example.demo.dto.userDto.UpdateUserRequest;
import com.example.demo.entity.User;

public interface UserService {

    User save(AddUserRequest request);

    User update(String userId, UpdateUserRequest request);

    void delete(String userId);
}
