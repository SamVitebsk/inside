package com.example.inside.controller;

import com.example.inside.AbstractTest;
import com.example.inside.resources.AuthRequest;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.hasKey;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@SqlGroup({
        @Sql(value = "classpath:sql/ClientService.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "classpath:sql/ClientServiceCleanData.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class ClientControllerTest extends AbstractTest {
    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void registration() {
        var request = new AuthRequest().setName("testName")
                                       .setPassword("password");

        given()
                .body(request)
                .contentType(ContentType.JSON)
        .when()
                .post("/registration")
        .then()
                .status(OK)
                .body("$", hasKey("token"));
    }

    @Test
    void registration_userAlreadyExists() {
        var request = new AuthRequest().setName("Alex")
                                       .setPassword("password");

        given()
                .body(request)
                .contentType(ContentType.JSON)
        .when()
                .post("/registration")
        .then()
                .status(INTERNAL_SERVER_ERROR);
    }

    @Test
    void authorization_withBadPassword() {
        var request = new AuthRequest().setName("Alex")
                                       .setPassword("bad password");

        given()
                .body(request)
                .contentType(ContentType.JSON)
        .when()
                .post("/authorization")
        .then()
                .status(INTERNAL_SERVER_ERROR);
    }

    @Test
    void authorization_withBadUsername() {
        var request = new AuthRequest().setName("bad name")
                                       .setPassword("password");
        given()
                .body(request)
                .contentType(ContentType.JSON)
        .when()
                .post("/authorization")
        .then()
                .status(INTERNAL_SERVER_ERROR);
    }

    @Test
    void authorization() {
        var request = new AuthRequest().setName("Alex")
                                       .setPassword("passwd");
        given()
                .body(request)
                .contentType(ContentType.JSON)
        .when()
                .post("/authorization")
        .then()
                .status(OK)
                .body("$", hasKey("token"));
    }
}
