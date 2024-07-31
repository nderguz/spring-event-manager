package org.example.eventmanager.events.db;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends CrudRepository<RegistrationEntity, Long> {

    @Transactional
    @Query("SELECT r FROM RegistrationEntity r WHERE r.userId = :user_id and r.event.id = :event_id")
    Optional<RegistrationEntity> findUserRegistration(
            @Param("user_id") Long userId,
            @Param("event_id") Long eventId
    );

    @Transactional
    @Query("SELECT r.event FROM RegistrationEntity r WHERE r.userId = :user_id")
    List<EventEntity> findUserEvents(
            @Param("user_id") Long userId
    );

    @Modifying
    @Transactional
    @Query("UPDATE RegistrationEntity SET registrationStatus = 1 WHERE userId = :user_id AND event = :event")
    void closeRegistration(
            @Param("user_id") Long userId,
            @Param("event") EventEntity event
    );

    @Modifying
    @Transactional
    @Query("UPDATE RegistrationEntity SET registrationStatus = 1 WHERE event = :event")
    void closeAllRegistrations(
            @Param("event") EventEntity event
    );
}
