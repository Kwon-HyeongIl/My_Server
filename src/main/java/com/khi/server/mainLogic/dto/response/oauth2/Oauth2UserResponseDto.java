package com.khi.server.mainLogic.dto.response.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class Oauth2UserResponseDto implements OAuth2User {

    private final Oauth2UserDto userDto;

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDto.getRole();
            }
        });

        return authorities;
    }

    @Override
    public String getName() {

        return userDto.getName();
    }

    public String getUserName() {

        return userDto.getUsername();
    }
}
