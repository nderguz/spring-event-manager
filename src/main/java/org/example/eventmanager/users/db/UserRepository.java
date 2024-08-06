package org.example.eventmanager.users.db;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);

    @Transactional
    @Query("""
      SELECT u.id FROM UserEntity u
      JOIN RegistrationEntity r 
      ON u.id = r.userId
      WHERE r.event.id = :event_id
""")
    Optional<List<Long>> findAllRegisteredUsersIdToEvent(
            @Param("event_id") Long eventId
    );
}
