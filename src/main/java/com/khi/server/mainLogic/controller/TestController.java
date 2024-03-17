package com.khi.server.mainLogic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/oauth2/access")
    public String oauth2Test() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("사용자 이메일 : {}", email);

        return "success";
    }
}
