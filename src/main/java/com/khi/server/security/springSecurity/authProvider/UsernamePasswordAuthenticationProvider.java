package com.khi.server.security.springSecurity.authProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * AuthenticationProvider를 이용하면 다양한 방식의 로그인을 처리할 수 있고, 보안상의 이점 존재
 * (예를 들어, 비밀번호로 로그인하는 경우, 지문으로 로그인 하는 경우, 인증코드로 로그인 하는 경우 등,
 * supports 메서드로 각각의 타입을 체크해서 맞는 타입의 authenticate 메서드가 호출되므로, 모든 경우의 맞춤형 인증 설계 가능)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        log.info("A");

        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails user = userDetailsService.loadUserByUsername(email);

        if (passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());

        } else {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
            /*
             * BadCredentialsException은 AuthenticationException을 상속받음
             * 즉, JwtAuthenticationEntryPoint가 캐치
             */
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        /*
         * AuthenticationManager의 AuthenticationProvider가 supports 메서드를 실행해서,
         * 전달된 authentication이 UsernamePasswordAuthenticationToken의 하위 타입인지 확인하고,boolean 값 반환
         * true일 경우, 스프링 시큐리티는 이 클래스의 authenticate 메서드를 호출
         */
    }
}
