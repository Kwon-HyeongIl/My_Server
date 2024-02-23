package com.khi.server.mainLogic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPageResponseDto {

    private String imageData; // Base64 인코딩된 이미지 데이터

    private String content;

    private String teamName;
}
