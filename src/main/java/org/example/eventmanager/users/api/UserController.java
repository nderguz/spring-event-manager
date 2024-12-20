package org.example.eventmanager.users.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.security.entities.JwtTokenResponse;
import org.example.eventmanager.security.entities.SignInRequest;
import org.example.eventmanager.security.entities.SignUpRequest;
import org.example.eventmanager.security.services.AuthenticationService;
import org.example.eventmanager.users.api.model.UserResponse;
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


    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> registerUser(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        return ResponseEntity.status(201)
                .body(userService.registerUser(signUpRequest));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserInfo(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(userService.findUserById(userId));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(
            @RequestBody @Valid SignInRequest signInRequest) {
        var token = authenticationService.authenticateUser(signInRequest);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }
}
