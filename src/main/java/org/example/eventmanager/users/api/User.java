package org.example.eventmanager.users.api;

import org.example.eventmanager.security.entities.Roles;

public record User (
        Long id,
        String login,
        String passwordHash,
        Integer age,
        Roles role
){
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
