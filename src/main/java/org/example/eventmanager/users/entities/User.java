package org.example.eventmanager.users.entities;

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


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", passwordHash='" + "****" + '\'' +
                ", age=" + age +
                ", role=" + role +
                '}';
    }

}
