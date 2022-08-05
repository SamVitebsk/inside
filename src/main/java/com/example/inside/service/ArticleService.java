package com.example.inside.service;

import com.example.inside.converter.ArticleToArticleResponseConverter;
import com.example.inside.entity.Article;
import com.example.inside.exception.UserNotFoundException;
import com.example.inside.repository.ArticleRepository;
import com.example.inside.repository.ClientRepository;
import com.example.inside.resources.ArticleRequest;
import com.example.inside.resources.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ClientRepository clientRepository;
    private final ArticleRepository articleRepository;
    private final ArticleToArticleResponseConverter toArticleResponseConverter;

    public ArticleResponse create(ArticleRequest request) {
        var client = clientRepository.findByName(request.getName().trim())
                                        .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь %s не найден", request.getName())));
        var article = new Article()
                .setClient(client)
                .setCreatedDate(LocalDateTime.now())
                .setMessage(request.getMessage().trim());

        return toArticleResponseConverter.convert(articleRepository.save(article));
    }

    public List<ArticleResponse> findLast(ArticleRequest request) {
        var count = Integer.parseInt(request.getMessage().trim().split(" ")[1]);

        return articleRepository.findLast(PageRequest.of(0, count, Sort.by("createdDate").descending()))
                                             .stream()
                                             .map(toArticleResponseConverter::convert)
                                             .collect(toList());
    }
}
