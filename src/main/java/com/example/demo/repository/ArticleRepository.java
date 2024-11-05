package com.example.demo.repository;

import com.example.demo.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

// repository
// DB관리, DB 접근 담당
// CRUD 명령 가능한 인터페이스
// DB에 접근하는 인터페이스 정의
// jpa 이용하여 CRUD 작업을 쉽게 구현

public interface ArticleRepository extends JpaRepository<Article, Long> {
//    @Query("SELECT a FROM Article a WHERE a.id > :cursor ORDER BY a.id ASC")
//    Slice<Article> findArticlesAfterCursor(@Param("cursor") Long cursor, @Param("limit") int limit);
    Slice<Article> findByIdGreaterThan(Long id, Pageable pageable);

    Boolean existsByUsername(String username);

}
