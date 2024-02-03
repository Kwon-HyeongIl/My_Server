package com.khi.server.security.springSecurity.utils;

import com.khi.server.constants.UserType;
import com.khi.server.mainLogic.entity.User;
import com.khi.server.mainLogic.repository.UserRepository;
import com.khi.server.security.springSecurity.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User findUser = repository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("이메일과 일치하는 사용자가 없습니다"));
        UserType authority = findUser.getAuthority();

        return new UserDetailsImpl(findUser, List.of(new SimpleGrantedAuthority(authority.name())));
        // Enum 타입인 UserType을 GrantedAuthority 타입 리스트로 변경
    }
}
