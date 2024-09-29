package com.example.demo.service.inter;

import com.example.demo.entity.Article;
import com.example.demo.dto.articleDto.AddArticleRequest;
import com.example.demo.dto.articleDto.ArticlesResponse;
import com.example.demo.dto.articleDto.UpdateArticleRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public interface ArticleService {

    Article save(AddArticleRequest request);

    Article findById(Long id);

    Page<ArticlesResponse> findAll(int page, int size);

    Slice<ArticlesResponse> findAllInfinity(Long lastId, int pageSize);

    void delete(Long id);

    Article update(Long id, UpdateArticleRequest request);
}
