package org.example.eventmanager.users.db;

import jakarta.persistence.*;
import lombok.*;
import org.example.eventmanager.security.entities.Roles;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "age")
    private Integer age;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;
}
