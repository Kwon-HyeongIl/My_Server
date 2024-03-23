package com.khi.server.security.handler;

import com.khi.server.dto.response.oauth2.Oauth2UserResponseDto;
import com.khi.server.security.utils.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Oauth2UserResponseDto oauth2User = (Oauth2UserResponseDto) authentication.getPrincipal();

        String token = jwtTokenProvider.createJwt(new UsernamePasswordAuthenticationToken(oauth2User.getEmail(), null, oauth2User.getAuthorities()));
        log.info("JWT 토큰이 발급되었습니다");

        response.addCookie(createCookie("Authorization", token));
        /*
         * 쿠키를 사용하는 이유:
         * 프론트단에서 anxios나 fetch와 같은 API 클라이언트가 아닌, 하이퍼링크 location.href로 요청을 보내기 때문
         */
        response.sendRedirect("http://localhost:3000/");
    }

    private Cookie createCookie(String key, String token) {

        Cookie cookie = new Cookie(key, token);
        cookie.setMaxAge(60*60*60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // Https 보안 기능

        return cookie;
    }
}
