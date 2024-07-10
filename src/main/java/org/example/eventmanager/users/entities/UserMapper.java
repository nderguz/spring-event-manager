package org.example.eventmanager.users.entities;


public class UserMapper {
    public UserDto toDto(User userDomain) {
        return new UserDto(
                userDomain.id(),
                userDomain.login(),
                userDomain.passwordHash(),
                userDomain.role()
        );
    }

    public User toDomain(UserDto dto) {
        return new User(
                dto.id(),
                dto.login(),
                dto.password(),
                dto.role()
        );
    }
}
