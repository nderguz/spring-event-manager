package org.example.eventmanager.unit.domain;

import org.example.eventmanager.security.entities.Roles;
import org.example.eventmanager.users.UniversalUserMapper;
import org.example.eventmanager.users.db.model.UserEntity;
import org.example.eventmanager.users.db.UserRepository;
import org.example.eventmanager.users.domain.model.UserInfo;
import org.example.eventmanager.users.domain.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UniversalUserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Валидный пользователь существует в БД")
    @Tags({
            @Tag("API"),
            @Tag("positive")
    })
    void isValidUserExistsByLogin() {
        var validTestUserEntity = UserEntity.builder()
                .login("validTestUser")
                .passwordHash("validPasswordHash")
                .role(Roles.USER)
                .id(3L)
                .build();
        when(userRepository.findByLogin("validTestUser")).thenReturn(Optional.ofNullable(validTestUserEntity));
        var user = userService.isUserExistsByLogin("validTestUser");
        verify(userRepository).findByLogin("validTestUser");
        assertTrue(user, "Такой пользователь найден");
    }

    @Test
    @DisplayName("Пользователь не найден в БД")
    @Tags({
            @Tag("API"),
            @Tag("negative")
    })
    void invalidUserIsNotExistsByLogin() {
        when(userRepository.findByLogin("invalidTestUser")).thenReturn(Optional.empty());
        var user = userService.isUserExistsByLogin("invalidTestUser");
        assertFalse(user);
    }

    @Test
    @DisplayName("Пользователь с логином null")
    @Tags({
            @Tag("API"),
            @Tag("negative")
    })
    void isUserExistsByLoginWithNull() {
        assertThrows(NullPointerException.class, () -> userService.isUserExistsByLogin(null),
                "Ожидается, что будет выброшена ошибка");
    }

    @Test
    @DisplayName("Проверка на пустой логин")
    @Tags({
            @Tag("API"),
            @Tag("negative")
    })
    void isUserExistsByLoginWithEmptyLogin() {
        var userExists = userService.isUserExistsByLogin("");
        assertFalse(userExists, "Ожидается, что пустой логин не соответствует пользователю");
    }

    @Test
    @DisplayName("Сохранение нового пользователя в БД успешно")
    @Tags({
            @Tag("API"),
            @Tag("positive")
    })
    void saveNewValidUserToDb(){
        var validUser = UserInfo.builder()
                .role(Roles.USER)
                .age(21)
                .passwordHash("hashedPassword")
                .login("validUserLogin")
                .build();
        var validUserEntity = UserEntity.builder()
                .role(Roles.USER)
                .age(21)
                .passwordHash("hashedPassword")
                .login("validUserLogin")
                .id(1L)
                .build();
        when(userRepository.save(validUserEntity)).thenReturn(validUserEntity);
        when(userMapper.domainToEntity(validUser)).thenReturn(validUserEntity);
        when(userMapper.entityToDomain(validUserEntity)).thenReturn(validUser);

        var savedUser = userService.saveUser(validUser);
        assertEquals(validUser.getLogin(), savedUser.login());
    }

    @Test
    @DisplayName("Получение валидного пользователя по логину")
    @Tags({
            @Tag("API"),
            @Tag("positive")
    })
    void getValidUserByLogin(){
        var validUser = UserInfo.builder()
                .role(Roles.USER)
                .age(21)
                .passwordHash("hashedPassword")
                .login("validUserLogin")
                .build();
        var validUserEntity = UserEntity.builder()
                .role(Roles.USER)
                .age(21)
                .passwordHash("hashedPassword")
                .login("validUserLogin")
                .id(1L)
                .build();
        when(userRepository.findByLogin("validUserLogin")).thenReturn(Optional.ofNullable(validUserEntity));
        when(userMapper.entityToDomain(validUserEntity)).thenReturn(validUser);

        var foundUser = userService.getUserByLogin("validUserLogin");

        assertEquals(validUser.getLogin(), foundUser.login());
    }

    @Test
    @DisplayName("Получение пользователья с логином null")
    @Tags({
            @Tag("API"),
            @Tag("negative")
    })
    void getValidUserByNullLogin(){
        assertThrows(NullPointerException.class, ()-> userService.getUserByLogin(null),
                "Ожидается, что будет выброшена ошибка");
    }
}