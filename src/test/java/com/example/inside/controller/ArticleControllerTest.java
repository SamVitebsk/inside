package com.example.inside.controller;

import com.example.inside.AbstractTest;
import com.example.inside.resources.ArticleRequest;
import com.example.inside.resources.ArticleResponse;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.HttpStatus.*;

@SqlGroup({
        @Sql(value = "classpath:sql/ArticleService.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "classpath:sql/ArticleServiceCleanData.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class ArticleControllerTest extends AbstractTest {
    private static final String TOKEN = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBbGV4IiwiaWF0IjoxNjU5Njk0NzUxLCJleHAiOjE2NjE4NDIyMzV9.7LQRxg37MjZHVV8JbGZr8U_Ttpnnrw2UXOWTcqUnJ8Q";

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void saveOrGetLast_withBadName() {
        var request = new ArticleRequest().setName("testName")
                                          .setMessage("test message");

        given()
                .body(request)
                .contentType(ContentType.JSON)
                .header("Authorization", TOKEN)
        .when()
                .post("/article")
        .then()
                .status(UNAUTHORIZED);
    }

    @Test
    void saveOrGetLast_withoutAuthorizationHeader() {
        var request = new ArticleRequest().setName("testName")
                                          .setMessage("test message");

        given()
                .body(request)
                .contentType(ContentType.JSON)
        .when()
                .post("/article")
        .then()
                .status(BAD_REQUEST);
    }

    @Test
    void saveOrGetLast_saveNewMessage() {
        var request = new ArticleRequest().setName("Alex")
                                          .setMessage("test message");

        given()
                .body(request)
                .contentType(ContentType.JSON)
                .header("Authorization", TOKEN)
        .when()
                .post("/article")
        .then()
                .status(CREATED)
                .body("$", hasKey("id"))
                .body("$", hasKey("createdDate"))
                .body("author", equalTo(request.getName()))
                .body("message", equalTo(request.getMessage()));
    }

    @Test
    void saveOrGetLast_getLast3Articles() {
        var request = new ArticleRequest().setName("Alex")
                                          .setMessage("history 3");

        List<ArticleResponse> articles = given()
                    .body(request)
                    .contentType(ContentType.JSON)
                    .header("Authorization", TOKEN)
                .when()
                    .post("/article")
                .then()
                    .status(OK)
                .extract().as(new TypeRef<>() {});


        assertFalse(articles.isEmpty());
        assertEquals(3, articles.size());
        assertEquals("message 7", articles.get(0).getMessage());
        assertEquals("message 6", articles.get(1).getMessage());
        assertEquals("message 5", articles.get(2).getMessage());
    }

    @Test
    void saveOrGetLast_invalidHistoryMessage() {
        var request = new ArticleRequest().setName("Alex")
                                          .setMessage("bad history 3");

        given()
            .body(request)
            .contentType(ContentType.JSON)
            .header("Authorization", TOKEN)
        .when()
            .post("/article")
        .then()
            .status(CREATED)
            .body("$", hasKey("id"))
            .body("$", hasKey("createdDate"))
            .body("author", equalTo(request.getName()))
            .body("message", equalTo("bad history 3"));
    }

    @Test
    void saveOrGetLast_invalidHistoryMessage2() {
        var request = new ArticleRequest().setName("Alex")
                                          .setMessage("history 3asd");

        given()
            .body(request)
            .contentType(ContentType.JSON)
            .header("Authorization", TOKEN)
        .when()
            .post("/article")
        .then()
            .status(CREATED)
            .body("$", hasKey("id"))
            .body("$", hasKey("createdDate"))
            .body("author", equalTo(request.getName()))
            .body("message", equalTo("history 3asd"));
    }

    @Test
    void saveOrGetLast_invalidHistoryMessage3() {
        var request = new ArticleRequest().setName("Alex")
                                          .setMessage("history 3 asd");

        given()
            .body(request)
            .contentType(ContentType.JSON)
            .header("Authorization", TOKEN)
        .when()
            .post("/article")
        .then()
            .status(CREATED)
            .body("$", hasKey("id"))
            .body("$", hasKey("createdDate"))
            .body("author", equalTo(request.getName()))
            .body("message", equalTo("history 3 asd"));
    }
}

