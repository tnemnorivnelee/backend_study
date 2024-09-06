package com.example.demo.dto.articleDto;

import lombok.Getter;

@Getter
public class UpdateArticleRequest {
    //업데이트에 필요한 변수 선언
    private String title;
    private String content;
}
