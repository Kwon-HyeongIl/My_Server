package com.khi.server.mainLogic.service;

import com.khi.server.mainLogic.constants.UserType;
import com.khi.server.mainLogic.dto.request.SigninRequestDto;
import com.khi.server.mainLogic.dto.request.SignupRequestDto;
import com.khi.server.mainLogic.entity.User;
import com.khi.server.security.jwt.utils.JwtProvider;
import com.khi.server.mainLogic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.key}")
    private String adminKey;

    public User createUser(SignupRequestDto requestUser) {

        String encodedPassword = passwordEncoder.encode(requestUser.getPassword());
        UserType role = checkRole(requestUser.getAdminKey());
        User user = new User(requestUser.getUsername(), requestUser.getEmail(), encodedPassword, role);
        repository.save(user);

        return user;
    }

    public String login(Authentication authentication) {

        log.info("{} 권한, 로그인 성공, 토큰 발급 시작", authentication.getAuthorities());
        String token = jwtProvider.createJwt(authentication);
        log.info("토큰이 정상적으로 발급되었습니다");

        return token;
    }

    //------------------------------------ private method ------------------------------------//

    private UserType checkRole(String inputAdminKey) {

        if (inputAdminKey.equals(adminKey)) {
            log.info("ADMIN 인증되었습니다");
            return UserType.ADMIN;
        } else {
            return UserType.USER;
        }
    }
}
