package org.example.eventmanager.users.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.eventmanager.users.entities.UserRepository;
import org.example.eventmanager.users.entities.UniversalUserMapper;
import org.example.eventmanager.users.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UniversalUserMapper universalUserMapper;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    public boolean isUserExistsByLogin(String login){
        return userRepository.findByLogin(login)
                .isPresent();
    }

    public User saveUser(User user){
        log.info("Saving user {}", user);
        var entity = universalUserMapper.domainToEntity(user);
        var savedUser = userRepository.save(entity);
        return universalUserMapper.entityToDomain(savedUser);
    }

    public User getUserByLogin(String login) {
        return userRepository
                .findByLogin(login)
                .map(universalUserMapper::entityToDomain)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by login %s".formatted(login)));

    }

    public User findUserById(Long userId) {
        return userRepository
                .findById(userId)
                .map(universalUserMapper::entityToDomain)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id %s".formatted(userId)));

    }
}