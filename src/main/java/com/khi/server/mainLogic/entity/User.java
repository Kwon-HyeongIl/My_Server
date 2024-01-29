package com.khi.server.mainLogic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    @Column(length = 10)
    private String userName;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    @OneToOne
    @JoinColumn(name = "MYPAGE_ID")
    private MyPage myPage;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;


    public User(String userName, String userId, String password) {
        this.userName = userName;
        this.email = userId;
        this.password = password;
    }

    public void setMyPage(MyPage myPage) {
        this.myPage = myPage;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
