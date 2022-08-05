package com.example.inside.repository;

import com.example.inside.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("select a from Article a")
    List<Article> findLast(Pageable pageable);
}
