package org.example.eventmanager.events.repository;

import org.example.eventmanager.events.model.EventEntity;
import org.example.eventmanager.events.model.RegistrationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends CrudRepository<RegistrationEntity, Long> {

    @Query("SELECT e FROM RegistrationEntity e WHERE e.userId = :user_id and e.eventId = :event_id")
    RegistrationEntity findUserRegistration (
            @Param("user_id") Long userId,
            @Param("event_id") Long eventId
    );

}
