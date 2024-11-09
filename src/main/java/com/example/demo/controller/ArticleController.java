package com.example.demo.controller;


import com.example.demo.dto.articleDTO.requestDTO.ArticleRequestDTO;
import com.example.demo.dto.articleDTO.requestDTO.UpdateArticleRequestDTO;
import com.example.demo.dto.articleDTO.responseDTO.AllArticleResponseDTO;
import com.example.demo.dto.articleDTO.responseDTO.ArticleResponseDTO;
import com.example.demo.dto.articleDTO.responseDTO.UpdateArticleResponseDTO;
import com.example.demo.service.impl.ArticleServiceImpl;
import com.example.demo.service.inter.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ArticleResponseDTO> createArticle(@RequestBody ArticleRequestDTO articleRequestDTO) {
        // @RequestBody -> http요청의 body 본문이 그대로 전달되도록
        // HttpServletRequest vs @RequestHeader 차이
        // 전자는 요청 전체를 전반적으로 다룰 때 사용
        // 후자는 특정 헤더 값에 간결하게 접근할 수 있으므로 RESTful API 에서 사용하면 좋음
        System.out.println("createArticle");

        ArticleResponseDTO savedArticleDTO = articleService.save(articleRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticleDTO);
    }

    // Read
    @GetMapping("/article/{id}") // {id} 경로변수, @PathVariable 어노테이션을 사용하여 메소드의 파라미터 값으로 가져와서 사용
    public ResponseEntity<ArticleResponseDTO> findArticle(@PathVariable Long id) {

        ArticleResponseDTO readArticleDTO = articleService.findById(id);

        return ResponseEntity.ok().body(readArticleDTO);
    }

    // Read All
    @GetMapping("/articles")
//    public ResponseEntity<Page<ArticlesResponse>> findAllArticle(@ModelAttribute ReadAllArticleRequest request) {
//
//        Page<ArticlesResponse> articles = articleService.findAll(request.getPage(), request.getSize());
//
//        return ResponseEntity.ok(articles);
//    }
    public ResponseEntity<Page<AllArticleResponseDTO>> findAllArticle(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "6") int size) {

        Page<AllArticleResponseDTO> readAllArticleDTO = articleService.findAll(page, size);

        return ResponseEntity.ok(readAllArticleDTO);
    }


    // Read All Infinity
    @GetMapping("/articles/scroll")
    public ResponseEntity<Slice<AllArticleResponseDTO>> findAllArticleInfiniteScroll(
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Slice<AllArticleResponseDTO> readAllArticleDTO = articleService.findAllInfinity(lastId, pageSize);

        return ResponseEntity.ok(readAllArticleDTO);
    }

    // Delete
    @DeleteMapping("/article/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {

        articleService.delete(id);

        return ResponseEntity.noContent().build();
    }

    // Update
    @PutMapping("/article/{id}")
    public ResponseEntity<UpdateArticleResponseDTO> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequestDTO request) {

        UpdateArticleResponseDTO updatedArticleDTO = articleService.update(id, request);

        return ResponseEntity.ok().body(updatedArticleDTO);
    }
}
