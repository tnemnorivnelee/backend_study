package com.example.demo.service.inter;

import com.example.demo.dto.articleDTO.requestDTO.ArticleRequestDTO;
import com.example.demo.dto.articleDTO.requestDTO.UpdateArticleRequestDTO;
import com.example.demo.dto.articleDTO.responseDTO.AllArticleResponseDTO;
import com.example.demo.dto.articleDTO.responseDTO.ArticleResponseDTO;
import com.example.demo.dto.articleDTO.responseDTO.UpdateArticleResponseDTO;
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
