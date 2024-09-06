package com.example.demo.dto;

import com.example.demo.domain.Article;
import lombok.Getter;

@Getter
public class ArticlesResponse {
    private final Long id;
    private final String title;
    private final String content;

    public ArticlesResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}


