package com.khi.server.mainLogic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyPage {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    private String teamName;

    public MyPage(String content) {
        this.content = content;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
