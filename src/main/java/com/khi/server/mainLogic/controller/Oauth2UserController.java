package com.khi.server.mainLogic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth2")
public class Oauth2UserController {

    @PostMapping("/google/signin/request")
    public void signinRequestGoogle() {

    }

    @PostMapping("/google/signin/accept")
    public void signinAcceptGoogle() {

    }

    @PostMapping("/naver/signin/request")
    public void signinRequestNaver() {

    }

    @PostMapping("/naver/signin/accept")
    public void signinAcceptNaver() {

    }
}
