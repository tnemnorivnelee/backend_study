package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException() {
      super("Resource already exists.");
    }

    // 사용자 정의 메시지 받는 생성자
    public AlreadyExistsException(String message) {
        super(message);
    }
}
