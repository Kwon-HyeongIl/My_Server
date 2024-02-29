package com.khi.server.mainLogic.dto.response.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Oauth2UserDto {

    private String role;
    private String name;
    private String username;
}
