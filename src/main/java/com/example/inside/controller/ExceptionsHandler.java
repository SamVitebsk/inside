package com.example.inside.controller;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> notFound(RuntimeException ex) {
        log.error("RuntimeException: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponse().setMessage(ex.getMessage()));
    }
}

@Data
@Accessors(chain = true)
class ErrorResponse {
    private String message;
}
