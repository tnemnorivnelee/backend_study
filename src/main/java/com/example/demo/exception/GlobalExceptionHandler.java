package com.example.demo.exception;

import com.example.demo.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  // AlreadyExistsException 예외 처리
  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException e, WebRequest request) {

    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            request.getDescription(false)
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
