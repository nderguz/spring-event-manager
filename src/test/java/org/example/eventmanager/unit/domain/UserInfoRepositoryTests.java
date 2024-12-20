package org.example.eventmanager.unit.domain;

import org.example.eventmanager.security.entities.Roles;
import org.example.eventmanager.users.db.model.UserEntity;
import org.example.eventmanager.users.db.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class UserInfoRepositoryTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.save(UserEntity.builder()
                .role(Roles.USER)
                .age(21)
                .passwordHash("hashPass")
                .login("testUser")
                .build());
    }

    /*
    1. Сохранение валидного пользователя
    2. Сохранение невалидного пользователя
    3. Удаление пользователя
    4. Получение пользователя
     */

    @Test
    @DisplayName("Получить пользователя из БД")
    void shouldReturnUserByLogin() {
        var user = repository.findByLogin("testUser");
        assertNotNull(user);
        assertNotNull(user.get().getId());
        assertNotNull(user.get().getLogin());
        assertEquals("testUser", user.get().getLogin());
        assertEquals("hashPass", user.get().getPasswordHash());
    }
}
