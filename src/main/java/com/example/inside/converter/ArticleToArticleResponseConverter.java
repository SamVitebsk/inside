package com.example.inside.converter;

import com.example.inside.entity.Article;
import com.example.inside.resources.ArticleResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArticleToArticleResponseConverter implements Converter<Article, ArticleResponse> {
    @Override
    public ArticleResponse convert(Article article) {
        return new ArticleResponse().setId(article.getId())
                .setMessage(article.getMessage())
                .setCreatedDate(article.getCreatedDate())
                .setAuthor(article.getClient().getName());

    }
}
