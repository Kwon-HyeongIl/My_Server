package com.khi.server.security.springSecurity.filter;

import com.khi.server.security.jwt.utils.JwtUtils;
import com.khi.server.security.jwt.authentication.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtValidateFilter extends OncePerRequestFilter { // 스프링 시큐리티는 필터가 한 번만 호출되는 것을 보장하지 않으므로, 필터가 요청당 한번만 실행하도록 보장하는 OncePerRequestFilter 구현

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (isSkip(request)) {

            filterChain.doFilter(request, response);
            /*
             * 인증 정보 설정을 하지 않고 doFilter 메서드를 호출 할 경우,
             * 스프링 시큐리티가 해당 요청이 인증되지 않았음을 감지하고 AuthenticationEntryPoint 실행
             * 또한, 현재 스레드의 사용자를 보안 컨텍스트에 등록
             */

            return;
        }

        String token = getToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        String email = jwtUtils.getUserEmail(token);
        String authority = jwtUtils.getUserAuthority(token);

        Authentication authentication = authenticationManager.authenticate(
                new JwtAuthenticationToken(email, List.of(new SimpleGrantedAuthority(authority)), token)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String getToken(String fullToken) {

        if (fullToken==null || !fullToken.startsWith("Bearer ")) {
            return null;
        }

        return fullToken.split(" ")[1];
    }

    private boolean isSkip(HttpServletRequest request){

        String[] skipUrls = {"/api/signup", "/api/signin"};

        if (Arrays.stream(skipUrls).anyMatch(url -> url.equals(request.getRequestURI()))) {
            return true;
        }

        return false;
    }
}

//1. 회원가입 -> 비밀번호 필터에서 null 반환
//2. 로그인 -> 비밀번호 필터에서 Auth 반환
//3. 토큰 인증 -> 비밀번호 필터에서 null 반환