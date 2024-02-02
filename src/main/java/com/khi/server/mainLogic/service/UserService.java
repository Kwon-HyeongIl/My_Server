package com.khi.server.mainLogic.service;

import com.khi.server.dto.request.LoginRequestDto;
import com.khi.server.dto.request.UserCreateRequestDto;
import com.khi.server.mainLogic.entity.User;
import com.khi.server.exception.LoginFailerException;
import com.khi.server.securityWithJwt.jwtUtils.JwtTokenProvider;
import com.khi.server.mainLogic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User createUser(UserCreateRequestDto requestUser) {

        String encodedPassword = passwordEncoder.encode(requestUser.getPassword());

        User user = new User(requestUser.getUsername(), requestUser.getEmail(), encodedPassword);
        repository.save(user);

        return user;
    }

    public String login(LoginRequestDto requestUser) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestUser.getEmail(), requestUser.getPassword())
        );

        log.info("로그인 성공, 토큰 발급 시작");

        return jwtTokenProvider.createJwt(authentication);


//        User user = repository.findUserByEmail(requestMember.getEmail()).orElseThrow(() -> new LoginFailerException("잘못된 Email 입니다"));
//
//        // 비밀번호의 해시를 비교
//        if (passwordEncoder.matches(requestMember.getPassword(), user.getPassword())) {
//            return jwtTokenProvider.createJwt(user.getEmail());
//        } else {
//            throw new LoginFailerException("Email과 Password가 일치하지 않습니다");
//        }
    }
}
