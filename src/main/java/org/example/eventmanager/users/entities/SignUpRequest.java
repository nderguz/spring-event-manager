package org.example.eventmanager.users.entities;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
        @NotBlank
        String login,
        @NotBlank
        String password,
        @Min(value = 18, message = "Age must be 18 or greater")
        Integer age
){
}
