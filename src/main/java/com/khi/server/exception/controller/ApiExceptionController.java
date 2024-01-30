package com.khi.server.exception.controller;

import com.khi.server.dto.response.ErrorResultResponseDto;
import com.khi.server.exception.LoginFailerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionController {

    @ExceptionHandler(LoginFailerException.class)
    public ResponseEntity<ErrorResultResponseDto> loginExHandler(LoginFailerException e) {

        return new ResponseEntity<>(new ErrorResultResponseDto("login fail", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResultResponseDto> validExHandler(MethodArgumentNotValidException e) {

        String defaultMessage = "defalut message: " + e.getMessage().split("default message")[2];
        return new ResponseEntity<>(new ErrorResultResponseDto("valid fail", defaultMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResultResponseDto> etcNotFoundExHandler(NullPointerException e) {

        return new ResponseEntity<>(new ErrorResultResponseDto("not found", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResultResponseDto> userNotFoundExHandler(UsernameNotFoundException e) {

        return new ResponseEntity<>(new ErrorResultResponseDto("user not found", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResultResponseDto> accessDeniedExHandler(AccessDeniedException e) {

        // 서비스 계층에서 발생하는 AccessDeniedException은 JwtAccessDeniedHandler가 캐치하지 못함
        return new ResponseEntity<>(new ErrorResultResponseDto("[service layer] access denied", e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
