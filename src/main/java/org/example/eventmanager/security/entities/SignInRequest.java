package org.example.eventmanager.security.entities;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest (
        @NotBlank
        String login,
        @NotBlank
        String password
){
}
