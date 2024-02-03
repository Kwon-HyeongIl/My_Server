package com.khi.server.securityWithJwt.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    // 스프링 시큐리티는 필터가 한 번만 호출되는 것을 보장하지 않으므로, 필터가 요청당 한번만 실행하도록 보장하는 OncePerRequestFilter 구현

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (isSkip(request)) {
            log.info("허가된 Url 입니다 {}", request.getRequestURI());

            filterChain.doFilter(request, response);
            return;
        }

        String token = getToken(request.getHeader(HttpHeaders.AUTHORIZATION));

        // 토큰 유효성 검증
        if (jwtUtils.validateToken(token)) {

            log.info("Jwt 토큰 유효성 검증을 통과했습니다");

            String email = jwtUtils.getUserEmail(token);
            String authority = jwtUtils.getUserAuthority(token);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(authority)));
            // (보안상의 이유 + 비밀번호를 이미 검증)의 이유로 비밀번호 매개값에 null 대입

            // 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            /*
             * 인증 정보 설정을 하지 않고 doFilter 메서드를 호출 할 경우,
             * 스프링 시큐리티가 해당 요청이 인증되지 않았음을 감지하고 AuthenticationEntryPoint 실행
             * 또한, 현재 스레드의 사용자를 보안 컨텍스트에 등록
             */
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(String fullToken) {
        if (fullToken==null || !fullToken.startsWith("Bearer ")) {
            return null;
        }

        return fullToken.split(" ")[1];
    }

    // 허가된 Url은 토큰 검증 메서드를 거치지 않음
    private boolean isSkip(HttpServletRequest request){
        String[] skipUrls = {"/api/signup", "/api/login"};

        if (Arrays.stream(skipUrls).anyMatch(url -> url.equals(request.getRequestURI()))) {
            return true;
        }

        return false;
    }
}
