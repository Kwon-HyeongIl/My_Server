package com.khi.server.security.springSecurity.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khi.server.mainLogic.dto.request.SigninRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        log.info("----------비밀번호 필터 실행");
        // 로그인 Api 요청일 경우에만 현재의 필터 로직 실행
        if (isSkip(request)) {
            return null;
        }

        try {
            SigninRequestDto signinRequestDto = objectMapper.readValue(request.getInputStream(), SigninRequestDto.class);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    signinRequestDto.getEmail(), signinRequestDto.getPassword());

            setDetails(request, usernamePasswordAuthenticationToken);
            return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);

        } catch (IOException e) {
            throw new AuthenticationServiceException("인증 중 오류 발생", e);
        }
    }

    private boolean isSkip(HttpServletRequest request) {

        if (!request.getRequestURI().equals("/api/signin")) {
            return true;
        }

        return false;
    }
}
