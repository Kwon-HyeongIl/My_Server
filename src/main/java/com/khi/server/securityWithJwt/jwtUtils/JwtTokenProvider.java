package com.khi.server.securityWithJwt.jwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    /*
     * <secretKey 변수에 final 키워드를 붙이지 못하는 이유>
     * final 필드는 반드시 클래스 선언 시점에 초기화 되어야 하는데, @Value 어노테이션을 사용하면 생성자 호출 이후에 값이 설정되므로 사용할 수 없음
     * 해결하려면 직접 생성자를 만들어서 @Autowired 하거나, final 키워드 제거
     */

    private Long expiredMs = 1000 * 60 * 60l;

    // Jwt 토큰 생성
    public String createJwt(String email) {

        return Jwts
                .builder()
                .claim("email", email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token, String secretKey) {
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

    public String getUserEmail(String token, String secretKey) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }
}
