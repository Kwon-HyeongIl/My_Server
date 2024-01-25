package com.khi.server.controller;

import com.khi.server.dto.request.LoginRequestDto;
import com.khi.server.dto.request.UserCreateRequestDto;
import com.khi.server.dto.response.TestResponseDto;
import com.khi.server.dto.response.TokenResponseDto;
import com.khi.server.dto.response.UserResponseDto;
import com.khi.server.entity.User;
import com.khi.server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService service;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserCreateRequestDto requsetUser) {

        User createdUser = service.createUser(requsetUser);
        return new ResponseEntity<>(new UserResponseDto(createdUser.getId()), HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto requestMember) {

        String token = service.login(requestMember);
        return new ResponseEntity<>(new TokenResponseDto(token), HttpStatus.OK);
    }

    @GetMapping("/user/authTest")
    public ResponseEntity<TestResponseDto> authTest(Authentication authentication) {

        return new ResponseEntity<>(new TestResponseDto("인증된 사용자입니다. " + authentication.getName()), HttpStatus.OK);
    }
}
