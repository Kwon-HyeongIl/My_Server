package com.khi.server.mainLogic.service;

import com.khi.server.mainLogic.dto.request.TeamRequestDto;
import com.khi.server.mainLogic.entity.MyPage;
import com.khi.server.mainLogic.entity.Team;
import com.khi.server.mainLogic.entity.User;
import com.khi.server.mainLogic.repository.TeamRepository;
import com.khi.server.mainLogic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional // 왜 Spring Data JPA가 변경을 자동으로 감지하지 못하는지 나중에 확인
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public Team createTeam(TeamRequestDto request) {

        User user = getAuthUser();
        isExistMyPage(user);

        Team team = new Team(request.getName());
        teamRepository.save(team);
        user.setTeam(team);

        setTeamNameToMyPage(user, team);

        return team;
    }

    public Team setTeam(TeamRequestDto request) {

        User user = getAuthUser();
        isExistMyPage(user);

        Team team = teamRepository.findTeamByName(request.getName()).orElseThrow(() -> new NullPointerException("입력하신 팀이 존재하지 않습니다"));
        user.setTeam(team);

        setTeamNameToMyPage(user, team);

        return team;
    }

    //------------------------------------ private method ------------------------------------//

    private User getAuthUser() {

        // 현재 실행중인 스레드에 대한 보안 컨텍스트에서 인증된 사용자의 정보를 가져옴
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("토큰 인증을 받은 사용자가 존재하지 않습니다"));
    }

    private void isExistMyPage(User user) {

        if (user.getMyPage() == null) {
            throw new AccessDeniedException("팀을 생성하기 전에, 마이페이지를 먼저 생성해야 됩니다");
        }
    }

    private void setTeamNameToMyPage(User user, Team team) {

        MyPage userMyPage = user.getMyPage();
        userMyPage.setTeamName(team.getName());
    }
}
