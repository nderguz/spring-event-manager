package org.example.eventmanager.users.entities;

public record User (
        Long id,
        String login,
        String passwordHash,
        String role
){
}
