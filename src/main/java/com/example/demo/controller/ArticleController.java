package com.example.demo.controller;


import com.example.demo.domain.Article;
import com.example.demo.dto.articleDto.AddArticleRequest;
import com.example.demo.dto.articleDto.ArticleResponse;
import com.example.demo.dto.articleDto.ArticlesResponse;
import com.example.demo.dto.articleDto.UpdateArticleRequest;
import com.example.demo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        // @RequestBody -> http요청의 body 본문이 그대로 전달되도록

        Article savedArticle = articleService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle); // CREATED(201) 코드 -> 요청이 성공적으로 처리되어 자원이 생성되었다는 성공 코드
    }

    // Read
    @GetMapping("/article/{id}") // {id} 경로변수, @PathVariable 어노테이션을 사용하여 메소드의 파라미터 값으로 가져와서 사용
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable Long id) {

        Article article = articleService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    // Read All
    @GetMapping("/articles")
    public ResponseEntity<Page<ArticlesResponse>> findAllArticle(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "6") int size) {

        Page<ArticlesResponse> articles = articleService.findAll(page, size);
        return ResponseEntity.ok(articles);
    }

    // Read All Infinity
    @GetMapping("/articles/scroll")
    public ResponseEntity<Slice<ArticlesResponse>> findAllArticleInfiniteScroll(
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Slice<ArticlesResponse> articles = articleService.findAllInfinity(lastId, pageSize);

        return ResponseEntity.ok(articles);
    }

    // Delete
    @DeleteMapping("/article/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {

        articleService.delete(id);

        return ResponseEntity.noContent().build();
    }

    // Update
    @PutMapping("/article/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {

        Article updatedArticle = articleService.update(id, request);

        return ResponseEntity.ok().body(updatedArticle);
    }
}
