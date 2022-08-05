package com.example.inside.resources;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ArticleResponse {
    private Long id;
    private String author;
    private String message;
    private LocalDateTime createdDate;
}
