package com.khi.server.security.springSecurity.utils;

import com.khi.server.mainLogic.constants.UserType;
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

/**
 * UserDetailsService를 스프링 빈으로 등록하면, 콘솔에 자동 생성된 암호가 출력 되지 않음
 * (자동 생성된 암호는 HTTP Basic 인증에 사용되는 암호)
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User findUser = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("이메일과 일치하는 사용자가 없습니다"));
        UserType authority = findUser.getAuthority();

        // Enum 타입인 UserType을 GrantedAuthority 타입 리스트로 변경
        return new UserDetailsImpl(findUser, List.of(new SimpleGrantedAuthority(authority.name())));
    }
}
