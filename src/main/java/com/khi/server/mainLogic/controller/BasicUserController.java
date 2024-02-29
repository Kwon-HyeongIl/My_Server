package com.khi.server.mainLogic.controller;

import com.khi.server.mainLogic.dto.request.SignupRequestDto;
import com.khi.server.mainLogic.dto.response.TokenResponseDto;
import com.khi.server.mainLogic.dto.response.UserResponseDto;
import com.khi.server.mainLogic.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/basic")
public class BasicUserController {

    private final UserService service;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody @Valid SignupRequestDto requsetUser) {

        UserResponseDto userDto = service.signup(requsetUser);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenResponseDto> signin() {

        TokenResponseDto tokenDto = service.signin();
        return new ResponseEntity<>(tokenDto, HttpStatus.OK);
    }
}
