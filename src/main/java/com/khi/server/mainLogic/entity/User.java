package com.khi.server.mainLogic.entity;

import com.khi.server.constants.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    @Column(length = 10)
    private String username;

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

    private UserType authority;


    public User(String username, String userId, String password, UserType authority) {
        this.username = username;
        this.email = userId;
        this.password = password;
        this.authority = authority;
    }

    public void setMyPage(MyPage myPage) {
        this.myPage = myPage;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
