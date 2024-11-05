package com.example.demo.dto.articleDTO.responseDTO;

import com.example.demo.entity.Article;
import lombok.Getter;

@Getter
public class AllArticleResponseDTO {
//    private final Long id;
    private final String username;
    private final String title;
    private final String content;

    public AllArticleResponseDTO(Article article) {
//        this.id = article.getId();
        this.username = article.getUsername();
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}


