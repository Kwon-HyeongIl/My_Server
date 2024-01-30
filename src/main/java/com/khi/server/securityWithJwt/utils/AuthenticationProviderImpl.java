package com.khi.server.securityWithJwt.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//public class AuthenticationProviderImpl implements AuthenticationProvider {
//
//    private final UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//
//        String email = authentication.getName();
//        String password = authentication.getCredentials().toString();
//
//        UserDetails user = userDetailsService.loadUserByUsername(email);
//
//        if (passwordEncoder.matches(password, user.getPassword())) {
//            // 토큰 발급
//
//        } else {
//            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
//            /*
//             * BadCredentialsException은 AuthenticationException을 상속받음
//             * 즉, JwtAuthenticationEntryPoint가 캐치
//             */
//        }
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return false;
//    }
//}
