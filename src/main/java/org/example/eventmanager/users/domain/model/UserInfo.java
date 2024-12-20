package org.example.eventmanager.users.domain.model;

import lombok.*;
import org.example.eventmanager.security.entities.Roles;

@AllArgsConstructor
@Builder
@Data
public class UserInfo {
    private Long id;
    private String login;
    private String passwordHash;
    private Integer age;
    private Roles role;
}


