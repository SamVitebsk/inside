package com.example.inside.controller;

import com.example.inside.resources.ArticleRequest;
import com.example.inside.service.ArticleService;
import com.example.inside.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<?> saveOrGetLast(@Valid @RequestBody ArticleRequest request,
                                           @RequestHeader("Authorization") String token) {
        if (!clientService.compareUsernames(token, request.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (request.getMessage().trim().matches("history \\d+")) {
            return ResponseEntity.ok(articleService.findLast(request));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(articleService.create(request));
    }
}
