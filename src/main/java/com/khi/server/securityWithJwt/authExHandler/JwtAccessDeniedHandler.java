package com.khi.server.securityWithJwt.authExHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
 * 인가 예외 처리 클래스
 * 스프링 시큐리티 필터체인이나 인증/인가 과정 중에 발생하는 AccessDeniedException 에외 등을 캐치
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);

        /*
         * 위의 클래스는 HttpServletResponse 객체를 직접 다루고 있으므로, HttpServletResponse 타입의 상태 코드를 사용하는 것이 더 직관적
         */
    }
}
