package com.khi.server.mainLogic.dto.response.oauth2;

import com.khi.server.mainLogic.constants.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Oauth2UserDto {

    private String username;
    private String name;
    private UserType authority;
}
