package com.example.demo.service.impl;

import com.example.demo.dto.articleDTO.requestDTO.ArticleRequestDTO;
import com.example.demo.dto.articleDTO.requestDTO.UpdateArticleRequestDTO;
import com.example.demo.dto.articleDTO.responseDTO.AllArticleResponseDTO;
import com.example.demo.dto.articleDTO.responseDTO.ArticleResponseDTO;
import com.example.demo.dto.articleDTO.responseDTO.UpdateArticleResponseDTO;
import com.example.demo.entity.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.service.inter.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

// Service
// 비즈니스 로직 처리, 정보(객체)를 처리하는 로직 구현
// 사용자 요구사항을 처리하는 곳
// controller 와 repository 사이의 미들웨어
// controller에서 전달받은 요청사항에 맞게 데이터를 가공하여 DB로 전달하거나 DB에서 전달받아 가공하여 유저에게 전달
// model이 DB에서 받아온 데이터를 전달받아서 가공하는 역할
// DB정보가 필요할 시 repository에게 요청

@RequiredArgsConstructor
@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    // Create
    @Override
    public ArticleResponseDTO save(ArticleRequestDTO request) {
        if(request.getTitle().isEmpty() || request.getContent().isEmpty()) {
            throw new NoSuchElementException("title or content is empty");
        }

        Article dtoToEntity = request.toEntity();

        Article savedArticle = articleRepository.save(dtoToEntity);

        return ArticleResponseDTO.builder()
                .title(savedArticle.getTitle())
                .content(savedArticle.getContent())
                .build();
    }

    // Read
    @Override
    @Transactional(readOnly = true)
    public ArticleResponseDTO findById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> notFoundId(id));

        return ArticleResponseDTO.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .build();
    }

    // Read All
    @Override
    @Transactional(readOnly = true)
    public Page<AllArticleResponseDTO> findAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Article> articles = articleRepository.findAll(pageable);

        return articles.map(AllArticleResponseDTO::new);
    }

    // Read All Infinity
    @Override
    @Transactional(readOnly = true)
    public Slice<AllArticleResponseDTO> findAllInfinity(Long lastId, int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, "updatedAt"));

        Slice<Article> articles;

        if (lastId == null) {
            System.out.println("lastId is null");
            articles = articleRepository.findAll(pageable);
        } else {
            System.out.println("lastId is " + lastId);
            articles = articleRepository.findByIdGreaterThan(lastId, pageable);
        }

        return articles.map(AllArticleResponseDTO::new);
    }


    // Delete
    @Override
    public void delete(Long id) {
        if(!articleRepository.existsById(id)) {
            throw notFoundId(id);
        }
        articleRepository.deleteById(id);
    }

    // Update
    @Override
    public UpdateArticleResponseDTO update(Long id, UpdateArticleRequestDTO request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> notFoundId(id));

        Article dtoToEntity = request.toEntity();

        article.update(dtoToEntity.getTitle(), dtoToEntity.getContent());

        return UpdateArticleResponseDTO.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .build();
    }

    private NoSuchElementException notFoundId(Long id) {
        return new NoSuchElementException("NOT FOUND ID : " + id);
    }
}