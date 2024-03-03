package com.khi.server.security.entity;

import com.khi.server.mainLogic.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * UserDetails는 스프링 시큐리티가 인증 과정에서 사용자 정보를 관리하는데 필요한 정보를 제공하는 표준 인터페이스
 * UserDetails를 사용함으로써, 미리 구현된 스프링 시큐리티와의 통합 기능(오버라이딩된 메서드 참조)을 직접 구현 없이 이용할 수 있음
 */
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;

    // 사용자는 권한을 하나만 갖지만, 오버라이드 메서드에 의해 어쩔수 없이 형식상 컬렉션 타입으로 설정
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // 식별자로 email 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
