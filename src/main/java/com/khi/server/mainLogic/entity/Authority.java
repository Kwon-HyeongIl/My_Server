package com.khi.server.mainLogic.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue
    private Long id;

    private String authority;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Override
    public String getAuthority() {
        return authority;
    }
}
