package org.example.eventmanager.users;

import lombok.AllArgsConstructor;
import org.example.eventmanager.users.entities.Roles;
import org.example.eventmanager.users.entities.SignUpRequest;
import org.example.eventmanager.users.entities.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRegistrationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(SignUpRequest signUpRequest) {
        if(userService.isUserExistsByLogin(signUpRequest.login())){
            throw new IllegalArgumentException("User with such login already exists");
        }
        var hashedPass = passwordEncoder.encode(signUpRequest.password());
        var user = new User(
                null,
                signUpRequest.login(),
                hashedPass,
                signUpRequest.age(),
                Roles.USER
        );
        return userService.saveUser(user);
    }
}
