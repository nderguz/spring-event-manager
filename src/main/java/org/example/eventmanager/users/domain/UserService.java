package org.example.eventmanager.users.domain;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.users.db.UserRepository;
import org.example.eventmanager.users.UniversalUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UniversalUserMapper universalUserMapper;

    @Transactional(readOnly = true)
    public boolean isUserExistsByLogin(String login){
        return userRepository.findByLogin(login)
                .isPresent();
    }

    @Transactional
    public User saveUser(User user){
        var entity = universalUserMapper.domainToEntity(user);
        var savedUser = userRepository.save(entity);
        return universalUserMapper.entityToDomain(savedUser);
    }

    @Transactional(readOnly = true)
    public User getUserByLogin(String login) {
        return userRepository
                .findByLogin(login)
                .map(universalUserMapper::entityToDomain)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by login %s".formatted(login)));

    }

    @Transactional(readOnly = true)
    public User findUserById(Long userId) {
        return userRepository
                .findById(userId)
                .map(universalUserMapper::entityToDomain)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id %s".formatted(userId)));

    }
}
