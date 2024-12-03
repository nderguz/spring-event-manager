package org.example.eventmanager.users;

import org.example.eventmanager.security.entities.Roles;
import org.example.eventmanager.users.api.UserDto;
import org.example.eventmanager.users.db.UserEntity;
import org.example.eventmanager.users.domain.User;
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
                userDomain.getRole()
        );
    }

    public User entityToDomain(UserEntity userEntity){
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getPasswordHash(),
                userEntity.getAge(),
                userEntity.getRole()
        );
    }
}
