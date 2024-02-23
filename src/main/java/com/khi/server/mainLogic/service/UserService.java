package com.khi.server.mainLogic.service;

import com.khi.server.mainLogic.constants.UserType;
import com.khi.server.mainLogic.dto.request.SignupRequestDto;
import com.khi.server.mainLogic.dto.response.TokenResponseDto;
import com.khi.server.mainLogic.dto.response.UserResponseDto;
import com.khi.server.mainLogic.entity.User;
import com.khi.server.security.authentication.JwtAuthToken;
import com.khi.server.security.utils.JwtTokenProvider;
import com.khi.server.mainLogic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.key}")
    private String adminKey;

    public UserResponseDto signup(SignupRequestDto requestUser) {

        String encodedPassword = passwordEncoder.encode(requestUser.getPassword());
        UserType role = checkRole(requestUser.getAdminKey());
        User user = new User(requestUser.getUsername(), requestUser.getEmail(), encodedPassword, role);
        repository.save(user);

        return new UserResponseDto(user.getId());
    }

    public TokenResponseDto signin() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthToken jwtAuth) {
            return new TokenResponseDto(jwtAuth.getToken());
        }

        log.info("주어진 Authentication이 JwtAuthToken으로 캐스팅 할 수 없습니다");
        throw new ClassCastException("형변환 오류입니다");
    }


    private UserType checkRole(String inputAdminKey) {

        if (inputAdminKey.equals(adminKey)) {
            log.info("ADMIN 인증되었습니다");
            return UserType.ADMIN;

        } else {
            return UserType.USER;
        }
    }
}
