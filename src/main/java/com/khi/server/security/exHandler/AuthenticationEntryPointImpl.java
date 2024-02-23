package com.khi.server.security.exHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증 예외 처리 클래스
 * 스프링 시큐리티 필터체인이나 인증/인가 과정 중에 발생하는 AuthenticationException 에외 등을 캐치
 * AuthenticationException 타입의 예외를 캐치해서 프로그램을 중단되지 않도록 하는 역할을 수행
 *
 * AuthenticationEntryPoint 기반 예외는 @RestControllerAdvice 보다
 * AuthenticationEntryPointImpl 에서 더 우선적으로 실행 됨
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) throws IOException {
        /*
         * 위의 클래스는 HttpServletResponse 객체를 직접 다루고 있으므로, HttpServletResponse 타입의 상태 코드를 사용하는 것이 더 직관적
         */

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(authEx.getMessage());
    }
}
