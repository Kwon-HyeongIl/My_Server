package com.khi.server.dto.request;

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
public class SignupRequestDto {

    @NotEmpty
    @Column(length = 10)
    private String username;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Column(length = 30)
    private String password;

    // 기본값 추가
    private String adminKey = "USER";
}
