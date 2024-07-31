package org.example.eventmanager.security.services;

import lombok.AllArgsConstructor;
import org.example.eventmanager.security.jwt.JwtTokenManager;
import org.example.eventmanager.users.domain.User;
import org.example.eventmanager.users.domain.UserService;
import org.example.eventmanager.security.entities.SignInRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
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

        if(!passwordEncoder.matches(signInRequest.password(), user.getPasswordHash())){
           throw new BadCredentialsException("Введен некорректный пароль");
        }

        return jwtTokenManager.generateToken(user);
    }

    public User getCurrentAuthenticatedUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            throw new IllegalStateException("Authentication not present");
        }
        return (User) authentication.getPrincipal();
//        var userName = authentication.getName();
//        return userService.getUserByLogin(userName);
    }
}
