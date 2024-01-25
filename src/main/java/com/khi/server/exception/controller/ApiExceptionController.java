package com.khi.server.exception.controller;

import com.khi.server.dto.response.ErrorResultResponseDto;
import com.khi.server.exception.LoginFailerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionController {

    @ExceptionHandler(LoginFailerException.class)
    public ResponseEntity<ErrorResultResponseDto> LoginExHandler(LoginFailerException e) {

        return new ResponseEntity<>(new ErrorResultResponseDto("login fail", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResultResponseDto> ValidExHandler(MethodArgumentNotValidException e) {

        String defaultMessage = "defalut message: " + e.getMessage().split("default message")[2];
        return new ResponseEntity<>(new ErrorResultResponseDto("valid fail", defaultMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResultResponseDto> NotFoundExHandler(NullPointerException e) {

        return new ResponseEntity<>(new ErrorResultResponseDto("not found", e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
