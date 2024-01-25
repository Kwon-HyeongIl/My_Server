package com.khi.server.controller;

import com.khi.server.dto.request.MyPageCreateRequestDto;
import com.khi.server.dto.response.UserResponseDto;
import com.khi.server.entity.User;
import com.khi.server.service.MyPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService service;

    @PostMapping("/mypage/create")
    public ResponseEntity<UserResponseDto> createMyPage(@RequestBody @Valid MyPageCreateRequestDto request) {

        User user = service.createMyPage(request);
        return new ResponseEntity<>(new UserResponseDto(user.getId()), HttpStatus.CREATED);
    }
}
