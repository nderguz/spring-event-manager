package org.example.eventmanager.users.domain;
import org.example.eventmanager.users.UniversalUserMapper;
import org.example.eventmanager.users.db.UserEntity;
import org.example.eventmanager.users.db.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private UniversalUserMapper universalUserMapper;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository, universalUserMapper);
    }

    @Test
    void isUserExistsByLogin() {
        String login = "user2";
        Mockito.when(userRepository.findByLogin(login)).thenReturn(Optional.of(new UserEntity()));
        var result = userService.isUserExistsByLogin(login);
        assertTrue(result);
    }
//    @Test
//    void saveUser() {
//    }
//
//    @Test
//    void getUserByLogin() {
//    }
//
//    @Test
//    void findUserById() {
//    }
}