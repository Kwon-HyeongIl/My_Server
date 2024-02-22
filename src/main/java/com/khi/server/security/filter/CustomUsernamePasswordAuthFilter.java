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
    /*
     * UsernamePasswordAuthenticationFilter와 같은 필터는 필터 체인을 받아서 doFilter 메서드로 request, response를 다음 필터로 전달하지 못하므로,
     * 다음 필터에서 request, response 값을 사용하지 못하므로 OncePerRequestFilter를 사용하는게 좋음
     */

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

        } catch (IOException e) { // ObjectMapper에서 발생하는 예외 캐치
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
