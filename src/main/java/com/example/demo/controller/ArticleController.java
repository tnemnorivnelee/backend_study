package com.example.demo.controller;


import com.example.demo.domain.Article;
import com.example.demo.dto.AddArticleRequest;
import com.example.demo.dto.ArticleResponse;
import com.example.demo.dto.UpdateArticleRequest;
import com.example.demo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller
// HTTP요청을 처리하고 응답하는 곳
// 사용자 요청이 진입하고 요청을 처리하는 곳, 실제는 service에서 처리
// Restful API를 정의하여 클라이언트와 상호작용
// 처리 후 service로 넘어감
// 해당 요청 url에 따라 적절한 view와 mapping 처리
// 적절한 ResponseEntity(dto)를 body에 담아 클라이언트에 반환

// 순서: 사용자 요청 -> controller -> service -> repository



@RequiredArgsConstructor // 초기화 되지 않은 필드에 대해 자동으로 생성자를 생성
@RestController // view가 필요없는 api만 지원하는 서비스에서 사용, Json 형태로 객체 데이터 반환
public class ArticleController {
    // 실제 작업이 수행되는 부분(jpa -> ArticleService 부분 가져와서 활용..?)

    private final ArticleService articleService;

    // ResponseEntity
    // 결과값, 상태코드, 헤더값, 오류코드 등을 상세하게 프론트 쪽으로 넘겨줄 수 있음


    // Create
    @PostMapping("/article")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {

        Article savedArticle = articleService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle); // CREATED(201) 코드 -> 요청이 성공적으로 처리되어 자원이 생성되었다는 성공 코드
    }

    // Read
    @GetMapping("/article/{id}") // {id} 경로변수, @PathVariable 어노테이션을 사용하여 메소드의 파라미터 값으로 가져와서 사용
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {

        Article article = articleService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    // Read All
    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticle() {

        List<ArticleResponse> articles = articleService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }

    // Delete
    @DeleteMapping("/article/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {

        articleService.delete(id);

        return ResponseEntity.noContent().build();
    }

    // Update
    @PutMapping("/article/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request) {

        Article updatedArticle = articleService.update(id, request);

        return ResponseEntity.ok().body(updatedArticle);
    }
}
