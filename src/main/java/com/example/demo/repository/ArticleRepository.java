package com.example.demo.repository;

import com.example.demo.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

// repository
// DB관리
// CRUD 명령 가능한 인터페이스
// DB에 접근하는 인터페이스 정의
// jpa 이용하여 CRUD 작업을 쉽게 구현

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
