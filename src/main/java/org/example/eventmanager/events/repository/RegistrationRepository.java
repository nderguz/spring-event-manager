package org.example.eventmanager.events.repository;

import org.example.eventmanager.events.model.registration.RegistrationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends CrudRepository<RegistrationEntity, Long> {

    @Query("SELECT r FROM RegistrationEntity r WHERE r.userId = :user_id and r.eventId = :event_id")
    RegistrationEntity findUserRegistration (
            @Param("user_id") Long userId,
            @Param("event_id") Long eventId
    );
    @Query("SELECT r FROM RegistrationEntity r WHERE r.userId = :user_id")
    List<RegistrationEntity> findByUserId(
            @Param("user_id") Long userId
    );

}
