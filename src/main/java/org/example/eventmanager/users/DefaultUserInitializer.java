package org.example.eventmanager.users;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.example.eventmanager.security.entities.Roles;
import org.example.eventmanager.users.domain.model.UserInfo;
import org.example.eventmanager.users.domain.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DefaultUserInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initUsers(){
        createUserIfNotExists("admin", "admin", Roles.ADMIN);
        createUserIfNotExists("user", "user", Roles.USER);
    }

    private void createUserIfNotExists(
            String login,
            String password,
            Roles role
    ){
        if(userService.isUserExistsByLogin(login)){
            return;
        }
        var hashedPass = passwordEncoder.encode(password);
        var user = UserInfo.builder()
                .login(login)
                .passwordHash(hashedPass)
                .age(21)
                .role(role)
                .build();
        userService.saveUser(user);
    }
}
