package com.khi.server.security.springSecurity.authProvider;

import com.khi.server.security.springSecurity.authentication.JwtAuthenticationToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    /*
     * <secretKey 변수에 final 키워드를 붙이지 못하는 이유>
     * final 필드는 반드시 클래스 선언 시점에 초기화 되어야 하는데, @Value 어노테이션을 사용하면 생성자 호출 이후에 값이 설정되므로 사용할 수 없음
     * 해결하려면 직접 생성자를 만들어서 @Autowired 하거나, final 키워드 제거
     */

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            String token = jwtAuthenticationToken.getToken();

            if (validateToken(token)) {
                return new JwtAuthenticationToken(jwtAuthenticationToken.getName(), jwtAuthenticationToken.getAuthorities());
            }

            throw new DisabledException("JWT 토큰 인증 오류입니다");
        }

        log.info("주어진 Authentication이 JwtAuthenticationToken으로 캐스팅 할 수 없습니다");
        throw new DisabledException("인증 오류입니다"); // AuthenticationException의 하위 타입
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
        /*
         * AuthenticationManager의 AuthenticationProvider가 supports 메서드를 실행해서,
         * 전달된 authentication이 JwtAuthenticationToken의 하위 타입인지 확인하고,boolean 값 반환
         * true일 경우, 스프링 시큐리티는 이 클래스의 authenticate 메서드를 호출
         */
    }

    private boolean validateToken(String token) {

        try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;

        } catch (SignatureException e) {
            log.info("유효하지 않은 Jwt 토큰입니다");

        } catch (IllegalArgumentException e) {
            log.info("Authorization이 없거나 잘못된 형식입니다");

        } catch (ExpiredJwtException e) {
            log.info("만료된 Jwt 토큰입니다");
        }

        return false;
    }
}
