package com.example.demo.dto.articleDTO.responseDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ArticleResponseDTO {
    // 글 불러오기 시 필요한 속성 선언

    private final Long id;
    private final String username;
    private final String email;
    private final String title;
    private final String content;

    @Builder
    public ArticleResponseDTO(Long id, String username, String email, String title, String content) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.title = title;
        this.content = content;
    }
}