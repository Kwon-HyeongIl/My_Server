package com.khi.server.security.filter;

import com.khi.server.security.utils.JwtUtils;
import com.khi.server.security.auth.JwtAuthToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtValidateFilter extends OncePerRequestFilter {
    // 스프링 시큐리티는 필터가 한 번만 호출되는 것을 보장하지 않으므로, 필터가 요청당 한번만 실행하도록 보장하는 OncePerRequestFilter 구현

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (isSkip(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getToken(request);

        String email = jwtUtils.getUserEmail(token);
        String authority = jwtUtils.getUserAuthority(token);

        Authentication authentication = authenticationManager.authenticate(
                new JwtAuthToken(email, List.of(new SimpleGrantedAuthority(authority)), token)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }


    private String getToken(HttpServletRequest request) {

        String token = null;

        // Header에 담긴 토큰 추출 (일반 로그인 방식)
        String fullToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (fullToken!=null && fullToken.startsWith("Bearer ")) {
            token = fullToken.split(" ")[1];
        }

        // Cookie에 담긴 토큰 추출 (OAuth2 소셜 로그인 방식)
        if (token == null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("Authorization")) {
                    token = cookie.getValue();
                }
            }
        }

        return token;
    }

    private boolean isSkip(HttpServletRequest request){

        String[] skipUrls = {"/api/basic/signup", "/api/basic/signin"};

        if (Arrays.stream(skipUrls).anyMatch(url -> url.equals(request.getRequestURI()))) {
            return true;
        }

        return false;
    }
}