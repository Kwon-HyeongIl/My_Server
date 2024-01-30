package com.khi.server.securityWithJwt.securityUtils;

import com.khi.server.mainLogic.entity.User;
import com.khi.server.mainLogic.repository.UserRepository;
import com.khi.server.securityWithJwt.securityEntity.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User findUser = repository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("이메일과 일치하는 사용자가 없습니다"));

        return new SecurityUser(findUser);
    }
}
