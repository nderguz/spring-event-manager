package org.example.eventmanager.users.entities;

public class EntityMapper {
    public UserEntity toEntity(User userDomain) {
        return new UserEntity(
                userDomain.id(),
                userDomain.login(),
                userDomain.passwordHash(),
                userDomain.role()
        );
    }

    public User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getPasswordHash(),
                userEntity.getRole()
        );
    }
}
