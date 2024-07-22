package org.example.eventmanager.users.api;

import org.example.eventmanager.security.entities.Roles;

public record UserDto (
        Long id,
        String login,
        Integer age,
        Roles role
){
}
