package com.example.demo.dto.articleDto;

import com.example.demo.domain.Article;
import lombok.Getter;

@Getter
public class ArticleResponse {
    // 글 불러오기 시 필요한 속성 선언

    private final String title;
    private final String content;

    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}