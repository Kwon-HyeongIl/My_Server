package com.khi.server.security.springSecurity.configuration;

import com.khi.server.security.springSecurity.authProvider.JwtAuthenticationProvider;
import com.khi.server.security.springSecurity.authProvider.UsernamePasswordAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@RequiredArgsConstructor
public class AuthenticationManagerConfig {

    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(usernamePasswordAuthenticationProvider)
                .authenticationProvider(jwtAuthenticationProvider)
                .build();
    }
}
