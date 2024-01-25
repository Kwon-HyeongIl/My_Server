package com.khi.server.mainLogic.service;

import com.khi.server.dto.request.MyPageCreateRequestDto;
import com.khi.server.mainLogic.entity.MyPage;
import com.khi.server.mainLogic.entity.User;
import com.khi.server.mainLogic.repository.MyPageRepository;
import com.khi.server.mainLogic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional // 왜 Spring Data JPA가 변경을 자동으로 감지하지 못하는지 나중에 확인
public class MyPageService {

    private final MyPageRepository myPageRepository;
    private final UserRepository userRepository;

    public User createMyPage(MyPageCreateRequestDto request) {

        MyPage myPage = new MyPage(request.getContent());
        myPageRepository.save(myPage);

        User user = getAuthUser();
        user.setMyPage(myPage);

        return user;
    }

    public MyPage getMyPage() {

        return getAuthUser().getMyPage();
    }

    private User getAuthUser() {

        // 현재 실행중인 스레드에 대한 보안 컨텍스트에서 인증된 사용자의 정보를 가져옴
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(() -> new NullPointerException("토큰 인증을 받은 사용자가 존재하지 않습니다"));

        return user;
    }
}
