package com.example.demo.dto.articleDTO.responseDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ArticleResponseDTO {
    // 글 불러오기 시 필요한 속성 선언

    private final String username;
    private final String title;
    private final String content;

    @Builder
    public ArticleResponseDTO(String username, String title, String content) {
        this.username = username;
        this.title = title;
        this.content = content;
    }
}