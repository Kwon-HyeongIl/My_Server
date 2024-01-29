package com.khi.server.mainLogic.controller;

import com.khi.server.dto.request.MyPageCreateRequestDto;
import com.khi.server.dto.response.MyPageResponseDto;
import com.khi.server.dto.response.UserResponseDto;
import com.khi.server.mainLogic.entity.MyPage;
import com.khi.server.mainLogic.entity.User;
import com.khi.server.mainLogic.service.MyPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        MyPage myPage = service.getMyPage();
        return new ResponseEntity<>(new MyPageResponseDto(myPage.getContent(), myPage.getTeamName()), HttpStatus.OK);
    }
}
