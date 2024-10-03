package com.example.demo.service.inter;

import com.example.demo.dto.articleDto.*;
import com.example.demo.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public interface ArticleService {

    ArticleResponseDTO save(ArticleRequestDTO request);

    ArticleResponseDTO findById(Long id);

    Page<AllArticleResponseDTO> findAll(int page, int size);

    Slice<AllArticleResponseDTO> findAllInfinity(Long lastId, int pageSize);

    void delete(Long id);

    UpdateArticleResponseDTO update(Long id, UpdateArticleRequestDTO request);
}
