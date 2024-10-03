package com.example.demo.dto.articleDto;

import com.example.demo.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ArticleResponseDTO {
    // 글 불러오기 시 필요한 속성 선언

    private final String title;
    private final String content;

    @Builder
    public ArticleResponseDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}