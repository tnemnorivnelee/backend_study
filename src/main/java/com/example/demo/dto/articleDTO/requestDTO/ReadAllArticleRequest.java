package com.example.demo.dto.articleDTO.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReadAllArticleRequest {
    private int page = 0;
    private int size = 6;
}
