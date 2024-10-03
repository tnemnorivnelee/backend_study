package com.example.demo.dto.articleDto;

import com.example.demo.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateArticleResponseDTO {
    private final String title;
    private final String content;

    @Builder
    public UpdateArticleResponseDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
