package com.khi.server.exception.controller;

import com.khi.server.dto.response.ErrorResultResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

/**
 * 메인 로직에서 try catch 구문으로 예외를 잡아서 처리할 필요 없이, 여기서 예외 처리를 하면 됨
 */
@RestControllerAdvice
public class ApiExceptionController {

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<ErrorResultResponseDto> classCastExHandler(ClassCastException e) {

        String defaultMessage = "defalut message: " + e.getMessage().split("default message")[2];
        return new ResponseEntity<>(new ErrorResultResponseDto("ClassCast Exception", defaultMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResultResponseDto> illegalStateExHandler(IllegalStateException e) {

        String defaultMessage = "defalut message: " + e.getMessage().split("default message")[2];
        return new ResponseEntity<>(new ErrorResultResponseDto("IllegalState Exception", defaultMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResultResponseDto> entityNotFoundExHandler(EntityNotFoundException e) {

        String defaultMessage = "defalut message: " + e.getMessage().split("default message")[2];
        return new ResponseEntity<>(new ErrorResultResponseDto("EntityNotFound Exception", defaultMessage), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResultResponseDto> ioExHandler(IOException e) {

        String defaultMessage = "defalut message: " + e.getMessage().split("default message")[2];
        return new ResponseEntity<>(new ErrorResultResponseDto("IO Exception", defaultMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // MultipartFile 제한 용량 초과 예외 캐치
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResultResponseDto> maxSizeExHandler(MaxUploadSizeExceededException e) {

        String defaultMessage = "defalut message: " + e.getMessage().split("default message")[2];
        return new ResponseEntity<>(new ErrorResultResponseDto("MaxUploadSizeExceeded Exception", defaultMessage), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    // 나머지 예외를 모두 캐치
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResultResponseDto> allExHandler(Exception e) {

        return new ResponseEntity<>(new ErrorResultResponseDto("Exception", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
