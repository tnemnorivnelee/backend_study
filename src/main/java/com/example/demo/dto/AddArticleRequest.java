package com.example.demo.dto;

import com.example.demo.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// dto, data transfer object
// 계층 간 데이터를 교환할 때 사용
// 데이터 저장 담당 클래스

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {
    // 글 작성 시 필요한 변수 선언

    private String title;
    private String content;

    public Article toEntity() {
        //빌더를 이용하여 dto를 entity로 만들어주는 메소드
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}