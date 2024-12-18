package org.example.eventmanager.users.domain;

import lombok.*;
import org.example.eventmanager.security.entities.Roles;

@AllArgsConstructor
@Builder
@Data
public class User{
    private Long id;
    private String login;
    private String passwordHash;
    private Integer age;
    private Roles role;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", age=" + age +
                ", role=" + role +
                '}';
    }
}


