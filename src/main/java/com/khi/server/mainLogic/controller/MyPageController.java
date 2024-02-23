package com.khi.server.mainLogic.controller;

import com.khi.server.mainLogic.dto.request.MyPageCreateRequestDto;
import com.khi.server.mainLogic.dto.response.MyPageResponseDto;
import com.khi.server.mainLogic.dto.response.UserResponseDto;
import com.khi.server.mainLogic.entity.MyPage;
import com.khi.server.mainLogic.entity.User;
import com.khi.server.mainLogic.service.MyPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @GetMapping("/mypage/get")
    public ResponseEntity<MyPageResponseDto> getMyPage() {

        MyPageResponseDto myPage = service.getMyPage();
        return new ResponseEntity<>(myPage, HttpStatus.OK);
    }

    @PostMapping("/mypage/image")
    public ResponseEntity<UserResponseDto> setImage(@RequestParam("image") MultipartFile file) throws IOException {

        User user = service.setImage(file);
        return new ResponseEntity<>(new UserResponseDto(user.getId()), HttpStatus.ACCEPTED);
    }
}
