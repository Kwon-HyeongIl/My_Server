package com.khi.server.security.configuration;

import com.khi.server.security.jwt.authExHandler.JwtAccessDeniedHandler;
import com.khi.server.security.jwt.authExHandler.JwtAuthenticationEntryPoint;
import com.khi.server.security.jwt.utils.JwtFilter;
import com.khi.server.security.jwt.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Value("${jwt.secret}")
    private String secretKey;
    /*
     * <secretKey 변수에 final 키워드를 붙이지 못하는 이유>
     * final 필드는 반드시 선언 시점에 초기화 되어야 하는데, @Value 어노테이션을 사용하면 생성자 호출 이후에 값이 설정되므로 사용할 수 없음
     * 해결하려면 직접 생성자를 만들어서 @Autowired 하거나, final 키워드 제거
     */

    // 비밀번호 해싱 알고리즘
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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

                // 인증, 인가 예외 처리 클래스 등록
                .exceptionHandling(exHandling -> exHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))

                // 회원가입, 로그인 제외 스프링 시큐리티 적용
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/signup", "/api/login").permitAll()
                        .anyRequest().authenticated())

                // 필터 체인에 필터 등록 (두 번째 매개변수의 필터 전에, 첫번째 매개변수 필터 실행)
                .addFilterBefore(new JwtFilter(jwtTokenProvider, secretKey), UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}
