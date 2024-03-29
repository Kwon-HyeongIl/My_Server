package com.khi.server.mainLogic.controller;

import com.khi.server.dto.request.MyPageCreateRequestDto;
import com.khi.server.dto.response.MyPageResponseDto;
import com.khi.server.dto.response.UserResponseDto;
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

        UserResponseDto userDto = service.createMyPage(request);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping("/mypage/get")
    public ResponseEntity<MyPageResponseDto> getMyPage() {

        MyPageResponseDto myPage = service.getMyPage();
        return new ResponseEntity<>(myPage, HttpStatus.OK);
    }

    @PostMapping("/mypage/image")
    public ResponseEntity<UserResponseDto> setImage(@RequestParam("image") MultipartFile file) throws IOException {

        UserResponseDto userDto = service.setImage(file);
        return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
    }
}
