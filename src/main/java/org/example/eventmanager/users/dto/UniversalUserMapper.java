package org.example.eventmanager.users.dto;

import org.example.eventmanager.security.entities.Roles;
import org.example.eventmanager.users.api.User;
import org.example.eventmanager.users.db.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UniversalUserMapper {
    public UserDto domainToDto(User userDomain){
        return new UserDto(
                userDomain.id(),
                userDomain.login(),
                userDomain.age(),
                userDomain.role()
        );
    }

    public UserEntity domainToEntity(User userDomain){
        return new UserEntity(
                userDomain.id(),
                userDomain.login(),
                userDomain.passwordHash(),
                userDomain.age(),
                userDomain.role().name()
        );
    }

    public User entityToDomain(UserEntity userEntity){
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getPasswordHash(),
                userEntity.getAge(),
                Roles.valueOf(userEntity.getRole())
        );
    }
}
