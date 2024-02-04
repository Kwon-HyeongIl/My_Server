package com.khi.server.security.springSecurity.authentication;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    @Getter
    private String token;

    // SecurityContext 저장용 생성자
    public JwtAuthenticationToken(String principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
    }

    // 토큰 검증용 생성자
    public JwtAuthenticationToken(String principal, Collection<? extends GrantedAuthority> authorities, String token) {
        super(authorities);
        this.principal = principal;
        this.token = token;
    }


    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

}
