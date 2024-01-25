package com.khi.server.mainLogic.service;

import com.khi.server.dto.request.LoginRequestDto;
import com.khi.server.dto.request.UserCreateRequestDto;
import com.khi.server.mainLogic.entity.User;
import com.khi.server.exception.LoginFailerException;
import com.khi.server.security.jwt.utils.JwtTokenProvider;
import com.khi.server.mainLogic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public User createUser(UserCreateRequestDto requestUser) {

        String encodedPassword = passwordEncoder.encode(requestUser.getPassword());

        User user = new User(requestUser.getUserName(), requestUser.getEmail(), encodedPassword);
        repository.save(user);

        return user;
    }

    public String login(LoginRequestDto requestMember) {

        User user = repository.findUserByEmail(requestMember.getEmail()).orElseThrow(() -> new LoginFailerException("잘못된 Email 입니다"));

        // 비밀번호의 해시를 비교
        if (passwordEncoder.matches(requestMember.getPassword(), user.getPassword())) {
            return jwtTokenProvider.createJwt(user.getEmail());
        } else {
            throw new LoginFailerException("Email과 Password가 일치하지 않습니다");
        }
    }
}
