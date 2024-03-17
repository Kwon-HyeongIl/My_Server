package com.khi.server.mainLogic.dto.response.oauth2;

import com.khi.server.mainLogic.constants.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Oauth2UserDto {

    private String username;
    private String oauth2Key;
    private String email;
    private UserType authority;
}
