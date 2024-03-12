package com.khi.server.security.service;

import com.khi.server.mainLogic.constants.UserType;
import com.khi.server.mainLogic.dto.response.oauth2.*;
import com.khi.server.mainLogic.entity.User;
import com.khi.server.mainLogic.repository.UserRepository;
import com.khi.server.security.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Oauth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User user = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Oauth2ResponseDto oauth2Response = null;

        if (registrationId.equals("google")) {
            oauth2Response = new GoogleResponseDto(user.getAttributes());

        } else if (registrationId.equals("naver")) {
            oauth2Response = new NaverResponseDto(user.getAttributes());

        } else {
            return null;
        }

        String username = oauth2Response.getProvider() + " " + oauth2Response.getProviderId();

        // 유저 데이터가 존재하는 경우
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username); // 유저가 없을 경우 UsernameNotFoundException 발생
            // 유저 데이터를 업데이트 하는 기능 추후 추가

            Oauth2UserDto userDto = new Oauth2UserDto(username, oauth2Response.getName(), UserType.USER);
            return new Oauth2UserResponseDto(userDto);

        // 유저 데이터가 존재하지 않는 경우 (회원 가입 수행)
        } catch (UsernameNotFoundException e) {
            User newUser = new User(username, oauth2Response.getName(), UserType.USER, oauth2Response.getEmail());
            userRepository.save(newUser);

            Oauth2UserDto userDto = new Oauth2UserDto(username, oauth2Response.getName(), UserType.USER);
            return new Oauth2UserResponseDto(userDto);
        }
    }
}
