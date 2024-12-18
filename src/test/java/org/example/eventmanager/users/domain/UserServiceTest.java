package org.example.eventmanager.users.domain;

import jakarta.persistence.EntityNotFoundException;
import org.example.eventmanager.security.entities.Roles;
import org.example.eventmanager.users.UniversalUserMapper;
import org.example.eventmanager.users.db.UserEntity;
import org.example.eventmanager.users.db.UserRepository;
import org.example.eventmanager.util.DBConnectionProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Mock
    private UserRepository userRepository;

    @Mock
    private UniversalUserMapper universalUserMapper;

    @InjectMocks
    private UserService userService;

    private UserEntity testUserEntity;
    private User testUser;

    @BeforeAll
    static void beforeAll(){
        postgres.start();
    }

    @AfterAll
    static void afterAll(){
        postgres.stop();
    }

    @BeforeEach
    public void setUp() {
        DBConnectionProvider connectionProvider = new DBConnectionProvider(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
                );

        userService = new UserService(userRepository, universalUserMapper);
        testUserEntity = UserEntity.builder()
                .id(1L)
                .login("user")
                .passwordHash("user")
                .age(18)
                .role(Roles.USER)
                .build();

        testUser = User.builder()
                .id(1L)
                .login("user")
                .passwordHash("user")
                .age(18)
                .role(Roles.USER)
                .build();
    }

    @Test
    void validUserSaved() {
        User user = userService.saveUser(testUser);
        System.out.println(testUser);
        var userList = userRepository.findByLogin(testUser.getLogin());
        assertTrue(userList.isPresent());
    }
//
//    @Test
//    void isUserExistsByLogin() {
//        when(userRepository.findByLogin("user")).thenReturn(Optional.ofNullable(userEntity));
//
//        Optional<UserEntity> foundUser = userRepository.findByLogin("user");
//
//        assertTrue(foundUser.isPresent());
//        assertEquals(userEntity.getLogin(), foundUser.get().getLogin());
//    }
//
//    @Test
//    void registerNewUser() {
//        User user = new User(1L, "user1", "passwordHash", 25, Roles.USER);
//        UserEntity userEntity = new UserEntity(1L, "user1", "passwordHash", 25, Roles.USER);
//
//        when(universalUserMapper.domainToEntity(any(User.class))).thenReturn(userEntity);
//        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
//        when(universalUserMapper.entityToDomain(any(UserEntity.class))).thenReturn(user);
//
//        User savedUser = userService.saveUser(user);
//
//        assertNotNull(savedUser);
//        assertEquals(user.getId(), savedUser.getId());
//        assertEquals(user.getLogin(), savedUser.getLogin());
//        verify(universalUserMapper, times(1)).domainToEntity(any(User.class));
//        verify(userRepository, times(1)).save(any(UserEntity.class));
//        verify(universalUserMapper, times(1)).entityToDomain(any(UserEntity.class));
//    }
//
//    @Test
//    void getUserByLogin() {
//        String login = "user1";
//        UserEntity userEntity = new UserEntity(1L, login, "passwordHash", 25, Roles.USER);
//        User user = new User(1L, login, "passwordHash", 25, Roles.USER);
//
//        when(userRepository.findByLogin(login)).thenReturn(Optional.of(userEntity));
//        when(universalUserMapper.entityToDomain(userEntity)).thenReturn(user);
//
//        User foundUser = userService.getUserByLogin(login);
//
//        assertNotNull(foundUser);
//        assertEquals(user.getId(), foundUser.getId());
//        assertEquals(user.getLogin(), foundUser.getLogin());
//        verify(userRepository, times(1)).findByLogin(login);
//        verify(universalUserMapper, times(1)).entityToDomain(userEntity);
//    }
//
//    @Test
//    void getUserByLogin_NotFound() {
//        String login = "nonexistent";
//        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());
//
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
//            userService.getUserByLogin(login);
//        });
//
//        assertEquals("Cannot find user by login nonexistent", exception.getMessage());
//        verify(userRepository, times(1)).findByLogin(login);
//    }
//
//    @Test
//    void findUserById() {
//        Long userId = 1L;
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
//        when(universalUserMapper.entityToDomain(userEntity)).thenReturn(user);
//
//        User foundUser = userService.findUserById(userId);
//
//        assertNotNull(foundUser);
//        assertEquals(user.getId(), foundUser.getId());
//        assertEquals(user.getLogin(), foundUser.getLogin());
//        verify(userRepository, times(1)).findById(userId);
//        verify(universalUserMapper, times(1)).entityToDomain(userEntity);
//    }
//
//    @Test
//    void findUserById_NotFound() {
//        Long userId = 999L;
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
//            userService.findUserById(userId);
//        });
//
//        assertEquals("Cannot find user by id 999", exception.getMessage());
//        verify(userRepository, times(1)).findById(userId);
//    }
}