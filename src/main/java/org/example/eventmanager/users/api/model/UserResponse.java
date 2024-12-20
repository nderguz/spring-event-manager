package org.example.eventmanager.users.api.model;

import lombok.Builder;
import org.example.eventmanager.security.entities.Roles;

@Builder
public record UserResponse(
        Long id,
        String login,
        Integer age,
        Roles role
){
}
