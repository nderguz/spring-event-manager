package org.example.eventmanager.events.db;

import jakarta.transaction.Transactional;
import org.example.eventmanager.events.db.model.EventEntity;
import org.example.eventmanager.events.db.model.RegistrationEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends CrudRepository<RegistrationEntity, Long> {

    @Query("SELECT r FROM RegistrationEntity r WHERE r.userId = :user_id and r.event.id = :event_id")
    Optional<RegistrationEntity> findUserRegistration(
            @Param("user_id") Long userId,
            @Param("event_id") Long eventId
    );

    @Query("SELECT r.event FROM RegistrationEntity r WHERE r.userId = :user_id")
    List<EventEntity> findUserEvents(
            @Param("user_id") Long userId
    );

    @Modifying
    @Query("UPDATE RegistrationEntity SET registrationStatus = \"CLOSED\" WHERE userId = :user_id AND event = :event")
    void closeRegistration(
            @Param("user_id") Long userId,
            @Param("event") EventEntity event
    );

    @Modifying
    @Query("UPDATE RegistrationEntity SET registrationStatus = \"CLOSED\" WHERE event = :event")
    void closeAllRegistrations(
            @Param("event") EventEntity event
    );

    @Query("""
    SELECT count(r)
    FROM RegistrationEntity r
    WHERE r.event.id = :eventId
""")
    Integer countRegistrations(@Param("eventId") Long eventId);
}
