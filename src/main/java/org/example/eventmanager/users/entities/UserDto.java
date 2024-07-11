package org.example.eventmanager.users.entities;

public record UserDto (
        Long id,
        String login,
        Integer age,
        Roles role
){
}
