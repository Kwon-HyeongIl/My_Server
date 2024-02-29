package com.khi.server.security.service;

import com.khi.server.mainLogic.dto.response.oauth2.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class Oauth2UserServiceImpl extends DefaultOAuth2UserService {

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

        Oauth2UserDto userDto = new Oauth2UserDto(username, oauth2Response.getName(), "USER");

        return new Oauth2UserResponseDto(userDto);
    }
}
