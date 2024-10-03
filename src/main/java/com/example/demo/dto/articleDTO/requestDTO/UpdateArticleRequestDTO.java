package com.example.demo.dto.articleDTO.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateArticleRequestDTO {
    private String title;
    private String content;
}
