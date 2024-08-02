package org.example.eventmanager.security.services;

import lombok.AllArgsConstructor;
import org.example.eventmanager.kafka.KafkaSender;
import org.example.eventmanager.security.entities.Roles;
import org.example.eventmanager.users.domain.UserService;
import org.example.eventmanager.security.entities.SignUpRequest;
import org.example.eventmanager.users.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRegistrationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final KafkaSender kafkaSender;

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
        kafkaSender.sendMessage("User", user.toString());

        return userService.saveUser(user);
    }
}
