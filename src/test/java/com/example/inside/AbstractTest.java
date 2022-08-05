package com.example.inside;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = {AbstractTest.Initializer.class})
public class AbstractTest {
    @Autowired
    protected MockMvc mockMvc;

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        private PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres")
                                                                                      .withTag("12.6"));

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext context) {
            postgres.start();

            TestPropertyValues.of(
                               "main.datasource.url=" + postgres.getJdbcUrl(),
                                      "main.datasource.username=" + postgres.getUsername(),
                                      "main.datasource.password=" + postgres.getPassword())
                              .applyTo(context);
        }
    }
}
