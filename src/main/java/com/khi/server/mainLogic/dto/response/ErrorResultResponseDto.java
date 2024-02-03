package com.khi.server.mainLogic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResultResponseDto {

    private String code;
    private String message;
}
