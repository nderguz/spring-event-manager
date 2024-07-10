package org.example.eventmanager.users.entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto (
        Long id,

        @Size(min = 5, max = 20, message = "Login must be from 5 to 20 symbols")
        @NotBlank(message = "Login cannot be empty")
        String login,

        @Size(max = 255, message = "Length of the password must be less than 255 symbols")
        String password,

        @NotBlank(message = "You must enter your age")

        String role
){
}
