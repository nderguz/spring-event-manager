package org.example.eventmanager.users.entities;

import org.example.eventmanager.security.entities.Roles;
import org.springframework.stereotype.Component;

@Component
public class UniversalUserMapper {
    public UserDto domainToDto(User userDomain){
        return new UserDto(
                userDomain.getId(),
                userDomain.getLogin(),
                userDomain.getAge(),
                userDomain.getRole()
        );
    }

    public UserEntity domainToEntity(User userDomain){
        return new UserEntity(
                userDomain.getId(),
                userDomain.getLogin(),
                userDomain.getPasswordHash(),
                userDomain.getAge(),
                userDomain.getRole().name()
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
