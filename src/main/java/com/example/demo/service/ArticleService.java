package com.example.demo.service;

import com.example.demo.domain.Article;
import com.example.demo.dto.AddArticleRequest;
import com.example.demo.dto.ArticlesResponse;
import com.example.demo.dto.UpdateArticleRequest;
import com.example.demo.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// Service
// 비즈니스 로직 처리, 정보(객체)를 처리하는 로직 구현
// 사용자 요구사항을 처리하는 곳
// controller 와 repository 사이의 미들웨어
// controller에서 전달받은 요청사항에 맞게 데이터를 가공하여 DB로 전달하거나 DB에서 전달받아 가공하여 유저에게 전달
// model이 DB에서 받아온 데이터를 전달받아서 가공하는 역할
// DB정보가 필요할 시 repository에게 요청

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    // Create
    public Article save(AddArticleRequest request) {
//        if(request.getTitle().isEmpty() || request.getContent().isEmpty()) {
//            throw new NoSuchElementException("title or content is empty");
//        }
        return articleRepository.save(request.toEntity());
    }

    // Read
    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("no exist : " + id));
    }

    // Read All
    public Page<ArticlesResponse> findAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Article> articles = articleRepository.findAll(pageable);

        return articles.map(ArticlesResponse::new);

//        Page<Article> articles = articleRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
//
//        return articles.getContent().stream().map(ArticlesResponse::new).collect(Collectors.toList());
    }



//    public List<Article> findAll() {
////        return articleRepository.findAll();
//        List<Article> articles = articleRepository.findAll();
//
//        if(articles.isEmpty()) {
//            throw new NoSuchElementException("Articles does not exits");
//        }
//        return articles;
//    }


    // Delete
    public void delete(Long id) {
        if(!articleRepository.existsById(id)) {
            throw new NoSuchElementException("no exist : " + id);
        }
        articleRepository.deleteById(id);
    }

    // Update
    @Transactional // 데이터를 하나의 묶음으로 처리, 수정 후 등록하는 사이에 오류가 발생하면 처음 상태로 되돌아감
    public Article update(Long id, UpdateArticleRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("no exist : " + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }
}