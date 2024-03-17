package com.khi.server.mainLogic.entity;

import com.khi.server.mainLogic.constants.UserType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(length = 10)
    private String username;

    private String oauth2Key;

    @NotEmpty
    @Email
    private String email;

//    @NotEmpty
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

    // Oauth2 용도
    public User(String username, String oauth2Key, UserType authority, String email) {
        this.username = username;
        this.oauth2Key = oauth2Key;
        this.authority = authority;
        this.email = email;
    }

    public void setMyPage(MyPage myPage) {
        this.myPage = myPage;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
