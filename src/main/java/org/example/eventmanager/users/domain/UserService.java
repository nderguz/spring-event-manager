package org.example.eventmanager.users.domain;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.security.entities.Roles;
import org.example.eventmanager.security.entities.SignUpRequest;
import org.example.eventmanager.users.UniversalUserMapper;
import org.example.eventmanager.users.api.model.UserResponse;
import org.example.eventmanager.users.db.UserRepository;
import org.example.eventmanager.users.domain.model.UserInfo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UniversalUserMapper universalUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public boolean isUserExistsByLogin(@NonNull String login) {
        return userRepository.findByLogin(login)
                .isPresent();
    }

    @Transactional
    public UserResponse saveUser(UserInfo user) {
        var entity = universalUserMapper.domainToEntity(user);
        var savedUser = userRepository.save(entity);
        return universalUserMapper.generateUserResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByLogin(@NonNull String login) {
        return userRepository
                .findByLogin(login)
                .map(universalUserMapper::generateUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by login %s".formatted(login)));
    }

    @Transactional(readOnly = true)
    public UserInfo getUserInfo(@NonNull String login){
        return userRepository
                .findByLogin(login)
                .map(universalUserMapper::entityToDomain)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by login %s".formatted(login)));
    }

    @Transactional(readOnly = true)
    public UserResponse findUserById(Long userId) {
        return userRepository
                .findById(userId)
                .map(universalUserMapper::generateUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id %s".formatted(userId)));

    }

    @Transactional
    public UserResponse registerUser(SignUpRequest request){
        if (isUserExistsByLogin(request.login())){
            throw new IllegalArgumentException("User with such login already exists");
        }
        var hashedPass = passwordEncoder.encode(request.password());
        var user = UserInfo.builder()
                .login(request.login())
                .passwordHash(hashedPass)
                .age(request.age())
                .role(Roles.USER)
                .build();
        return saveUser(user);
    }
}
