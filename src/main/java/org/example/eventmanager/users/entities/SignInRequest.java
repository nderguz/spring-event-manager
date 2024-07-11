package org.example.eventmanager.users.entities;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest (
        @NotBlank
        String login,
        @NotBlank
        String password
){
}
