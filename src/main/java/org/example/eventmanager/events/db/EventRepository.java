package org.example.eventmanager.events.db;

import org.example.eventmanager.events.domain.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    @Query("SELECT e FROM EventEntity e WHERE e.status = :status")
    List<EventEntity> findAllByStatus(@Param("status") EventStatus status);

    @Query("SELECT e FROM EventEntity e WHERE e.ownerId = :user_id")
    List<EventEntity> findAllUserEvents(@Param("user_id") Long userId);

    @Query("SELECT e.registrations FROM EventEntity e JOIN e.registrations r WHERE e.id = :eventId AND r.registrationStatus = 0")
    List <RegistrationEntity> getEventOpenedRegistrations(
            @Param("eventId") Long eventId
    );

    @Query("""
            SELECT e FROM EventEntity e 
            WHERE (:name IS NULL OR e.name LIKE %:name%) 
            AND (:placesMin IS NULL OR e.maxPlaces >= :placesMin) 
            AND (:placesMax IS NULL OR e.maxPlaces <= :placesMax) 
            AND (CAST(:dateStartAfter as date) IS NULL OR e.date >= :dateStartAfter) 
            AND (CAST(:dateStartBefore as date) IS NULL OR e.date <= :dateStartBefore) 
            AND (:costMin IS NULL OR e.cost >= :costMin) 
            AND (:costMax IS NULL OR e.cost <= :costMax) 
            AND (:durationMin IS NULL OR e.duration >= :durationMin) 
            AND (:durationMax IS NULL OR e.duration <= :durationMax) 
            AND (:locationId IS NULL OR e.locationId = :locationId) 
            AND (:eventStatus IS NULL OR e.status = :eventStatus)
            """)
    List<EventEntity> findEvents(
            @Param("name") String name,
            @Param("placesMin") Integer placesMin,
            @Param("placesMax") Integer placesMax,
            @Param("dateStartAfter") LocalDateTime dateStartAfter,
            @Param("dateStartBefore") LocalDateTime dateStartBefore,
            @Param("costMin") Long costMin,
            @Param("costMax") Long costMax,
            @Param("durationMin") Integer durationMin,
            @Param("durationMax") Integer durationMax,
            @Param("locationId") Long locationId,
            @Param("eventStatus") EventStatus eventStatus
    );
}
