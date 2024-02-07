package com.khi.server.security.springSecurity.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khi.server.security.springSecurity.authProvider.JwtAuthenticationProvider;
import com.khi.server.security.springSecurity.authProvider.UsernamePasswordAuthenticationProvider;
import com.khi.server.security.springSecurity.exHandler.AccessDeniedHandlerImpl;
import com.khi.server.security.springSecurity.exHandler.AuthenticationEntryPointImpl;
import com.khi.server.security.springSecurity.filter.CustomUsernamePasswordAuthenticationFilter;
import com.khi.server.security.springSecurity.filter.JwtValidateFilter;
import com.khi.server.security.jwt.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;
    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

//        AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);

        return httpSecurity

                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)

                .authenticationProvider(usernamePasswordAuthenticationProvider)
                .authenticationProvider(jwtAuthenticationProvider)

                // 세션을 사용하지 않기 때문에 무상태로 설정
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 인증, 인가 예외 처리 클래스 등록
                .exceptionHandling(exHandling -> exHandling
                        .authenticationEntryPoint(authenticationEntryPointImpl)
                        .accessDeniedHandler(accessDeniedHandlerImpl))

                // 회원가입, 로그인 제외 스프링 시큐리티 적용
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/signup").permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN") // 스프링 시큐리티가 SecurityContext의 Authentication에서 Authorities 값을 꺼내서 확인
                        .anyRequest().authenticated())

                // 필터 체인에 필터 등록 (두 번째 매개변수의 필터 전에, 첫번째 매개변수 필터 실행)
                .addFilterBefore(new CustomUsernamePasswordAuthenticationFilter(objectMapper, authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtValidateFilter(jwtUtils, authenticationManager), CustomUsernamePasswordAuthenticationFilter.class)
                /*
                 * formLogin()을 추가해야 UsernamePasswordAuthenticationFilter가 필터에 추가되는데, 현재는 formLogin()을 추가 안했으므로,
                 * UsernamePasswordAuthenticationFilter는 형식상의 위치를 나타냄
                 */

                .build();
    }
}
