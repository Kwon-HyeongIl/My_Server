package com.khi.server.security.filter;

import com.khi.server.security.utils.JwtUtils;
import com.khi.server.security.auth.JwtAuthToken;
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

        String token = getToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        String email = jwtUtils.getUserEmail(token);
        String authority = jwtUtils.getUserAuthority(token);

        Authentication authentication = authenticationManager.authenticate(
                new JwtAuthToken(email, List.of(new SimpleGrantedAuthority(authority)), token)
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