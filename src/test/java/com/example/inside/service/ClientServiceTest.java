package com.example.inside.service;

import com.example.inside.AbstractTest;
import com.example.inside.exception.UserNameExistsException;
import com.example.inside.exception.UserNotFoundException;
import com.example.inside.resources.ArticleRequest;
import com.example.inside.resources.AuthRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.*;

@SqlGroup({
        @Sql(value = "classpath:sql/ClientService.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "classpath:sql/ClientServiceCleanData.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class ClientServiceTest extends AbstractTest {
    @Autowired
    private ClientService clientService;

    @Test
    void create() {
        var authRequest = new AuthRequest()
                .setName("test name")
                .setPassword("password");

        var authResponse = clientService.create(authRequest);

        assertNotNull(authResponse.getToken());
        assertNotNull(clientService.compareUsernames(authResponse.getToken(), authRequest.getName()));
    }

    @Test
    void create_nameAlreadyExists() {
        var authRequest = new AuthRequest()
                .setName("Alex")
                .setPassword("password");

        assertThrows(
                UserNameExistsException.class,
                () -> clientService.create(authRequest)
        );
    }

    @Test
    void auth_withBadPasswordUserNotFound() {
        var authRequest = new AuthRequest()
                .setName("Alex")
                .setPassword("bad pass");

        assertThrows(
                UserNotFoundException.class,
                () -> clientService.auth(authRequest)
        );
    }

    @Test
    void auth_withBadUsernameAndPasswordUserNotFound() {
        var authRequest = new AuthRequest()
                .setName("bad name")
                .setPassword("123");

        assertThrows(
                UserNotFoundException.class,
                () -> clientService.auth(authRequest)
        );
    }

    @Test
    void auth() {
        var authRequest = new AuthRequest()
                .setName("Alex")
                .setPassword("passwd");

        var authResponse = clientService.auth(authRequest);

        assertNotNull(authResponse.getToken());
        assertTrue(clientService.compareUsernames(authResponse.getToken(), authRequest.getName()));
    }

    @Test
    void compareUsernames_false() {
        var articleRequest = new ArticleRequest().setName("bad user");
        var token = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjU5NjQzODcxLCJleHAiOjE2Njk2NDM4NzF9.kBBdLoxxLXIA0HF1G2HPVMwS5n31z6ULKB-McqGkF90";

        assertFalse(clientService.compareUsernames(token, articleRequest.getName()));
    }

    @Test
    void compareUsernames_true() {
        var articleRequest = new ArticleRequest().setName("user");
        var token = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjU5NjQzODcxLCJleHAiOjE2Njk2NDM4NzF9.kBBdLoxxLXIA0HF1G2HPVMwS5n31z6ULKB-McqGkF90";

        assertTrue(clientService.compareUsernames(token, articleRequest.getName()));
    }
}
