package com.khi.server.mainLogic.exception.controller;

import com.khi.server.mainLogic.dto.response.ErrorResultResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionController {

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<ErrorResultResponseDto> ClassCastExHandler(ClassCastException e) {

        String defaultMessage = "defalut message: " + e.getMessage().split("default message")[2];
        return new ResponseEntity<>(new ErrorResultResponseDto("ClassCast Exception", defaultMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResultResponseDto> IllegalStateExHandler(IllegalStateException e) {

        String defaultMessage = "defalut message: " + e.getMessage().split("default message")[2];
        return new ResponseEntity<>(new ErrorResultResponseDto("IllegalState Exception", defaultMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResultResponseDto> EntityNotFoundExHandler(EntityNotFoundException e) {

        String defaultMessage = "defalut message: " + e.getMessage().split("default message")[2];
        return new ResponseEntity<>(new ErrorResultResponseDto("EntityNotFound Exception", defaultMessage), HttpStatus.NOT_FOUND);
    }
}
