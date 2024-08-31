package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.dto.AddUserRequest;
import com.example.demo.repository.CheckUserRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Service
// 비즈니스 로직 처리, 정보(객체)를 처리하는 로직 구현
// 사용자 요구사항을 처리하는 곳
// controller 와 repository 사이의 미들웨어
// controller에서 전달받은 요청사항에 맞게 데이터를 가공하여 DB로 전달하거나 DB에서 전달받아 가공하여 유저에게 전달
// model이 DB에서 받아온 데이터를 전달받아서 가공하는 역할
// DB정보가 필요할 시 repository에게 요청

@RequiredArgsConstructor
@Service
public class UserService {

    private final CheckUserRepository checkUserRepository;
    private final UserRepository userRepository;

    // Create
    public User save(AddUserRequest request) {

        if(!checkUserRepository.existsByUserId(request.getUserId())) {
            return userRepository.save(request.toEntity());
        } else {
            return null;
        }
    }

//    // Read
//    public User findById(long id) {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("no exist : " + id));
//    }
//
//    // Read All
//    public List<User> findAll() {
//        return userRepository.findAll();
//    }
//
//    // Delete
//    public void delete(long id) {
//        userRepository.deleteById(id);
//    }
//
//    // Update
//    @Transactional // 데이터를 하나의 묶음으로 처리, 수정 후 등록하는 사이에 오류가 발생하면 처음 상태로 되돌아감
//    public User update(long id, UpdateUserRequest request) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("no exist : " + id));
//
//        user.update(request.getPwd());
//
//        return user;
//    }
}