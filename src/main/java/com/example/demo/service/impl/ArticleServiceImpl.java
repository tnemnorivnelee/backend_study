package com.example.demo.service.impl;

import com.example.demo.common.Role;
import com.example.demo.dto.articleDTO.requestDTO.ArticleRequestDTO;
import com.example.demo.dto.articleDTO.requestDTO.UpdateArticleRequestDTO;
import com.example.demo.dto.articleDTO.responseDTO.AllArticleResponseDTO;
import com.example.demo.dto.articleDTO.responseDTO.ArticleResponseDTO;
import com.example.demo.dto.articleDTO.responseDTO.UpdateArticleResponseDTO;
import com.example.demo.dto.userDto.CustomUserDetails;
import com.example.demo.entity.Article;
import com.example.demo.entity.User;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.inter.ArticleService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

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
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // Create
    @Override
    public ArticleResponseDTO save(ArticleRequestDTO articleRequestDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) principal;

        String tokenEmail = customUserDetails.getUsername();
        Role role = Role.valueOf(customUserDetails.getAuthorities().iterator().next().getAuthority());

        System.out.println("add article user token email : " + tokenEmail + " role : " + role);

        // 로그인 확인

        // 3. accessToken 유저, 권한 추출
        User user = userRepository.findByEmail(tokenEmail)
                .orElseThrow(() -> new NoSuchElementException("email not found"));

        // 4. 권한 검사
        if (!role.equals(Role.ROLE_ADMIN)) {
            throw new IllegalArgumentException("user not authorized");
        }

        Article dtoToEntity = articleRequestDTO.toEntity(user.getUsername(), tokenEmail);

        Article savedArticle = articleRepository.save(dtoToEntity);

        return ArticleResponseDTO.builder()
                .id(savedArticle.getId())
                .username(user.getUsername())
                .email(user.getEmail())
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
                .id(article.getId())
                .username(article.getUsername())
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

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) principal;

        String tokenEmail = customUserDetails.getUsername();

        // 4. 권한 검사 (게시글 email, 토큰 email 같은 지 확인)
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> notFoundId(id));;
        String articleEmail = article.getEmail();

        if (!tokenEmail.equals(articleEmail)) {
            throw new IllegalArgumentException("email mismatch");
        }

        articleRepository.deleteById(id);
    }

    // Update
    @Override
    public UpdateArticleResponseDTO update(Long id, UpdateArticleRequestDTO request) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) principal;

        String tokenEmail = customUserDetails.getUsername();

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> notFoundId(id));

        // 토큰 email 뽑아서 해당 글 email 같은지 검사
        if (!tokenEmail.equals(article.getEmail())) {
            throw new IllegalArgumentException("email mismatch");
        }

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