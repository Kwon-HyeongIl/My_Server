package com.khi.server.mainLogic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String type;

    private byte[] data;
    /*
     * TINYBLOB(약 0.2KB), BLOB(약 64KB), MEDIUMBLOB(약 16MB), LONGBLOB(약 4GB)
     * SQL 구문으로 위의 4가지 중 하나를 선택해야됨 (기본값은 TINYBLOB)
     */

    public Image(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
}
