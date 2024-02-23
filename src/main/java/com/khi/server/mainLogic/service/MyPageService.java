package com.khi.server.mainLogic.service;

import com.khi.server.mainLogic.dto.request.MyPageCreateRequestDto;
import com.khi.server.mainLogic.dto.response.MyPageResponseDto;
import com.khi.server.mainLogic.dto.response.UserResponseDto;
import com.khi.server.mainLogic.entity.Image;
import com.khi.server.mainLogic.entity.MyPage;
import com.khi.server.mainLogic.entity.User;
import com.khi.server.mainLogic.repository.ImageRepository;
import com.khi.server.mainLogic.repository.MyPageRepository;
import com.khi.server.mainLogic.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Transactional // 왜 Spring Data JPA가 변경을 자동으로 감지하지 못하는지 나중에 확인
public class MyPageService {

    private final MyPageRepository myPageRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public UserResponseDto createMyPage(MyPageCreateRequestDto request) {

        MyPage myPage = new MyPage(request.getContent());
        myPageRepository.save(myPage);

        User user = getAuthUser();
        user.setMyPage(myPage);

        return new UserResponseDto(user.getId());
    }

    public MyPageResponseDto getMyPage() {

        MyPage myPage = getAuthUser().getMyPage();
        String encodedImageData = Base64.getEncoder().encodeToString(myPage.getImage().getData());

        return new MyPageResponseDto(encodedImageData, myPage.getContent(), myPage.getTeamName());
    }

    public UserResponseDto setImage(MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        byte[] data = file.getBytes();

        User user = getAuthUser();
        MyPage myPage = user.getMyPage();
        Image image = new Image(fileName, fileType, data);

        imageRepository.save(image);
        myPage.setImage(image);

        return new UserResponseDto(user.getId());
    }

    private User getAuthUser() {

        // 현재 실행중인 스레드에 대한 보안 컨텍스트에서 인증된 사용자의 정보를 가져옴
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("토큰 인증을 받은 사용자가 존재하지 않습니다"));
    }
}
