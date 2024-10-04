package com.example.demo.dto.articleDTO.requestDTO;

import com.example.demo.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateArticleRequestDTO {
    private String title;
    private String content;

    public Article toEntity() {
        // 빌더를 이용하여 dto를 entity로 만들어주는 메소드
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}
