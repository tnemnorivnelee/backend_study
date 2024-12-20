package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;

// Entity
// DB와 상호작용하는 객체 정의

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 객체의 무결성을 유지하기 위해 AllArgs 대신 NoArgs 이노테이션 사용
public class Article extends Date {
    // DB에 저장할 데이터 지정

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID AUTO_INCREMENT
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;


    @Builder
    public Article(String username, String email, String title, String content) {
        this.username = username;
        this.email = email;
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
