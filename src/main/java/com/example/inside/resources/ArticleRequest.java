package com.example.inside.resources;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class ArticleRequest {
    @NotBlank
    private String name ;

    @NotBlank
    private String message;
}
