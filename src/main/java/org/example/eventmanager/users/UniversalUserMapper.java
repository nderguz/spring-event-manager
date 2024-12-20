package org.example.eventmanager.users;

import org.example.eventmanager.users.api.model.UserResponse;
import org.example.eventmanager.users.db.model.UserEntity;
import org.example.eventmanager.users.domain.model.UserInfo;
import org.springframework.stereotype.Component;

@Component
public class UniversalUserMapper {

    public UserEntity domainToEntity(UserInfo userDomain) {
        return UserEntity.builder()
                .id(userDomain.getId())
                .login(userDomain.getLogin())
                .passwordHash(userDomain.getPasswordHash())
                .age(userDomain.getAge())
                .role(userDomain.getRole())
                .build();
    }

    public UserInfo entityToDomain(UserEntity userEntity) {
        return UserInfo.builder()
                .id(userEntity.getId())
                .login(userEntity.getLogin())
                .passwordHash(userEntity.getPasswordHash())
                .age(userEntity.getAge())
                .role(userEntity.getRole())
                .build();
    }

    public UserResponse generateUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .login(userEntity.getLogin())
                .age(userEntity.getAge())
                .role(userEntity.getRole())
                .build();
    }
}
