package com.example.demo.dto.articleDTO.responseDTO;

import com.example.demo.entity.Article;
import lombok.Getter;

@Getter
public class AllArticleResponseDTO {
//    private final Long id;
    private final String title;
    private final String content;

    public AllArticleResponseDTO(Article article) {
//        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}


