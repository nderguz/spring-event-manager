package org.example.eventmanager.users.services;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.security.services.AuthenticationService;
import org.example.eventmanager.security.jwt.JwtTokenManager;
import org.example.eventmanager.security.entities.JwtTokenResponse;
import org.example.eventmanager.security.services.UserRegistrationService;
import org.example.eventmanager.security.entities.SignInRequest;
import org.example.eventmanager.security.entities.SignUpRequest;
import org.example.eventmanager.users.entities.UniversalUserMapper;
import org.example.eventmanager.users.entities.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserRegistrationService userRegistrationService;
    private final UniversalUserMapper universalUserMapper;
    private final JwtTokenManager jwtTokenManager;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid SignUpRequest signUpRequest
    ){
        log.info("Request for register new user: {}", signUpRequest.login());
        var user = userRegistrationService.registerUser(signUpRequest);
        var token = jwtTokenManager.generateToken(user);

        return ResponseEntity.status(201)
                .body(universalUserMapper.domainToDto(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserInfo(
            @PathVariable Long userId
    ){
        log.info("Request for retrieve user info for user: {}", userId);
        var user = userService.findUserById(userId);
        return ResponseEntity.ok(universalUserMapper.domainToDto(user));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(
            @RequestBody @Valid SignInRequest signInRequest){
        log.info("Get request for authenticate user: {}", signInRequest.login());
        var token = authenticationService.authenticateUser(signInRequest);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }
}
