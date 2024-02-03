package com.khi.server.mainLogic.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SigninRequestDto {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Column(length = 30)
    private String password;
}
