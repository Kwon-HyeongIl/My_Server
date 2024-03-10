package com.khi.server.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khi.server.security.authProvider.JwtAuthProvider;
import com.khi.server.security.authProvider.UsernamePasswordAuthProvider;
import com.khi.server.security.handler.CustomSuccessHandler;
import com.khi.server.security.handler.securityExHandler.AccessDeniedHandlerImpl;
import com.khi.server.security.handler.securityExHandler.AuthenticationEntryPointImpl;
import com.khi.server.security.filter.CustomUsernamePasswordAuthFilter;
import com.khi.server.security.filter.JwtValidateFilter;
import com.khi.server.security.service.Oauth2UserServiceImpl;
import com.khi.server.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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

    private final UsernamePasswordAuthProvider usernamePasswordAuthProvider;
    private final JwtAuthProvider jwtAuthProvider;

    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final AccessDeniedHandlerImpl accessDeniedHandler;

    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;

    private final Oauth2UserServiceImpl oauth2UserService;
    private final CustomSuccessHandler customSuccessHandler;

    @Bean
    public AuthenticationManager authManagerProvider(HttpSecurity http) throws Exception {

        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(usernamePasswordAuthProvider)
                .authenticationProvider(jwtAuthProvider)
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        /*
         * AuthenticationManager를 빈으로 등록하고 필드로 주입 받으면 의존성 문제가 생기므로,
         * 등록한 AuthentciationManager를 빈 메서드의 매개값으로 주입 받으면 의존성 문제를 해결할 수 있음
         */

        return http

                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                // 세션을 사용하지 않기 때문에 무상태로 설정
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 인증, 인가 예외 처리 클래스 등록
                .exceptionHandling(exHandling -> exHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))

                // Oauth2
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(oauth2UserService))
                        .successHandler(customSuccessHandler)
                )

                // 회원가입, 로그인 제외 스프링 시큐리티 적용
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/basic/signup").permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/basic/signin","/api/user/**").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated())
                /*
                 * hasAuthority 메서드는 스프링 시큐리티가 SecurityContext의 Authentication에서 Authorities 값을 꺼내서 확인
                 */

                // 필터 체인에 필터 등록 (두 번째 매개변수의 필터 전에, 첫번째 매개변수 필터 실행)
                .addFilterBefore(new CustomUsernamePasswordAuthFilter(objectMapper, authManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtValidateFilter(jwtUtils, authManager), CustomUsernamePasswordAuthFilter.class)
                /*
                 * formLogin()을 추가해야 UsernamePasswordAuthenticationFilter가 필터에 추가되는데, 현재는 formLogin()을 추가 안했으므로,
                 * UsernamePasswordAuthenticationFilter는 형식상의 위치를 나타냄
                 */

                .build();
    }
}
