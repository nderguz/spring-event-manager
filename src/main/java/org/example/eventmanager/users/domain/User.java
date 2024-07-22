package org.example.eventmanager.users.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.eventmanager.security.entities.Roles;

@AllArgsConstructor
@Getter
@Setter
public class User{
    private Long id;
    private String login;
    private String passwordHash;
    private Integer age;
    private Roles role;
}
