package com.example.demo.service.inter;

import com.example.demo.dto.userDto.UpdateUserRoleRequest;
import com.example.demo.dto.userDto.UserRequestDTO;
import com.example.demo.dto.userDto.UpdateUserPasswordRequest;

public interface UserService {

    void save(UserRequestDTO request);

    void updatePassword(UpdateUserPasswordRequest request);

    void delete();

    void updateRole(UpdateUserRoleRequest request);
}
