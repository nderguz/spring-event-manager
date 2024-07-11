package org.example.eventmanager.security;

import lombok.AllArgsConstructor;
import org.example.eventmanager.users.api.UserService;
import org.example.eventmanager.security.entities.SignInRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public String authenticateUser(SignInRequest signInRequest) {
        if(!userService.isUserExistsByLogin(signInRequest.login())){
            throw new BadCredentialsException("Введено некорректное имя пользователя");
        }

        var user = userService.getUserByLogin(signInRequest.login());

        if(!passwordEncoder.matches(signInRequest.password(), user.passwordHash())){
           throw new BadCredentialsException("Введен некорректный пароль");
        }

        return jwtTokenManager.generateToken(user);
    }
}
