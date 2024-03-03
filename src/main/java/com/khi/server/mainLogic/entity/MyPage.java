package com.khi.server.mainLogic.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "IMAGE_ID")
    private Image image;

    private String content;

    private String teamName;

    public MyPage(String content) {
        this.content = content;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
