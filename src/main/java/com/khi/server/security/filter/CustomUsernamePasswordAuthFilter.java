package com.khi.server.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khi.server.mainLogic.dto.request.SigninRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomUsernamePasswordAuthFilter extends OncePerRequestFilter {
    // 스프링 시큐리티는 필터가 한 번만 호출되는 것을 보장하지 않으므로, 필터가 요청당 한번만 실행하도록 보장하는 OncePerRequestFilter 구현

    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (isSkip(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            SigninRequestDto signinRequestDto = objectMapper.readValue(request.getInputStream(), SigninRequestDto.class);

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signinRequestDto.getEmail(), signinRequestDto.getPassword()
            ));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (IOException e) {
            throw new AuthenticationServiceException("인증 중 오류 발생", e);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isSkip(HttpServletRequest request) {

        if (!request.getRequestURI().equals("/api/signin")) {
            return true;
        }

        return false;
    }
}
