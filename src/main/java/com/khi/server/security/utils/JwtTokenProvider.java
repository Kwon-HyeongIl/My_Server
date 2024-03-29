package com.khi.server.security.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
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
    public String createJwt(Authentication auth) {

        // 형식상으로 있는 Collection 타입에서 하나의 권한만 가져옴
        String authority = auth.getAuthorities().iterator().next().getAuthority();

        return Jwts
                .builder()
                .claim("email", auth.getName())
                .claim("authority", authority)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
