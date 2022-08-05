package com.example.inside.service;

import com.example.inside.AbstractTest;
import com.example.inside.exception.UserNotFoundException;
import com.example.inside.resources.ArticleRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.*;

@SqlGroup({
        @Sql(value = "classpath:sql/ArticleService.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "classpath:sql/ArticleServiceCleanData.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class ArticleServiceTest extends AbstractTest {
    @Autowired
    private ArticleService articleService;

    @Test
    void create_userNotFound() {
        var request = new ArticleRequest()
                .setName("bad name")
                .setMessage("test message");

        assertThrows(
                UserNotFoundException.class,
                () -> articleService.create(request)
        );
    }

    @Test
    void create() {
        var name = "Alex";
        var message = "test message";
        var request = new ArticleRequest()
                .setName(name)
                .setMessage(message);

        var response = articleService.create(request);

        assertNotNull(response.getId());
        assertEquals(name, response.getAuthor());
        assertEquals(message, response.getMessage());
    }

    @Test
    void create_getLast3Articles() {
        var request = new ArticleRequest()
                .setName("Alex")
                .setMessage("history 3");

        var articles = articleService.findLast(request);

        assertEquals(3, articles.size());
        assertEquals("message 7", articles.get(0).getMessage());
        assertEquals("message 6", articles.get(1).getMessage());
        assertEquals("message 5", articles.get(2).getMessage());
    }

    @Test
    void create_getLast5Articles() {
        var request = new ArticleRequest()
                .setName("Alex")
                .setMessage("history 5");

        var articles = articleService.findLast(request);

        assertEquals(5, articles.size());
    }
}
