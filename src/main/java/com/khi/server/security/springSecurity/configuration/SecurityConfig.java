package com.khi.server.security.springSecurity.configuration;

import com.khi.server.security.springSecurity.exHandler.AccessDeniedHandlerImpl;
import com.khi.server.security.springSecurity.exHandler.AuthenticationEntryPointImpl;
import com.khi.server.security.jwt.JwtFilter;
import com.khi.server.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtUtils jwtUtils;
    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;
    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider)
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity

                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)

                // 세션을 사용하지 않기 때문에 무상태로 설정
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // AuthenticationProvider 등록
                .authenticationProvider(authenticationProvider)

                // 인증, 인가 예외 처리 클래스 등록
                .exceptionHandling(exHandling -> exHandling
                        .authenticationEntryPoint(authenticationEntryPointImpl)
                        .accessDeniedHandler(accessDeniedHandlerImpl))

                // 회원가입, 로그인 제외 스프링 시큐리티 적용
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/signup", "/api/signin").permitAll()
//                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())

                // 필터 체인에 필터 등록 (두 번째 매개변수의 필터 전에, 첫번째 매개변수 필터 실행)
                .addFilterBefore(new JwtFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}
