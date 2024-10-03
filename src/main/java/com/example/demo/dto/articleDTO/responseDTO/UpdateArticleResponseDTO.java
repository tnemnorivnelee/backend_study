package com.example.demo.dto.articleDTO.responseDTO;

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
