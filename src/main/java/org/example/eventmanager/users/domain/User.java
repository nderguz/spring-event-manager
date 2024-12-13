package org.example.eventmanager.users.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.eventmanager.security.entities.Roles;

@AllArgsConstructor
@Getter
@Setter
@Builder
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


