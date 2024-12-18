package org.example.eventmanager.users.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.security.services.AuthenticationService;
import org.example.eventmanager.security.entities.JwtTokenResponse;
import org.example.eventmanager.security.services.UserRegistrationService;
import org.example.eventmanager.security.entities.SignInRequest;
import org.example.eventmanager.security.entities.SignUpRequest;
import org.example.eventmanager.users.UniversalUserMapper;
import org.example.eventmanager.users.domain.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Slf4j
public class UserController {

    private final UserRegistrationService userRegistrationService;
    private final UniversalUserMapper universalUserMapper;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid SignUpRequest signUpRequest
    ){
        var user = userRegistrationService.registerUser(signUpRequest);
        return ResponseEntity.status(201)
                .body(universalUserMapper.domainToDto(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserInfo(
            @PathVariable Long userId
    ){
        var user = userService.findUserById(userId);
        return ResponseEntity.ok(universalUserMapper.domainToDto(user));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(
            @RequestBody @Valid SignInRequest signInRequest){
        var token = authenticationService.authenticateUser(signInRequest);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }
}
