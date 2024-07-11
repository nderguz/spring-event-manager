package org.example.eventmanager.users.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.eventmanager.security.AuthenticationService;
import org.example.eventmanager.security.JwtTokenManager;
import org.example.eventmanager.security.entities.JwtTokenResponse;
import org.example.eventmanager.security.UserRegistrationService;
import org.example.eventmanager.security.entities.SignInRequest;
import org.example.eventmanager.security.entities.SignUpRequest;
import org.example.eventmanager.users.dto.UniversalUserMapper;
import org.example.eventmanager.users.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
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
