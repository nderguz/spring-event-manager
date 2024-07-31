package org.example.eventmanager.events.db;

import jakarta.transaction.Transactional;
import org.example.eventmanager.events.domain.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    @Transactional
    @Query("SELECT e FROM EventEntity e WHERE e.status = :status")
    List<EventEntity> findAllByStatus(@Param("status") EventStatus status);

    @Query("SELECT e FROM EventEntity e WHERE e.ownerId = :user_id")
    List<EventEntity> findAllUserEvents(@Param("user_id") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE EventEntity e SET e.status = :status where e.id = :id")
    void changeEventStatus(
            @Param("id") Long eventId,
            @Param("status") EventStatus eventStatus
    );

    @Transactional
    @Query("SELECT e.registrations FROM EventEntity e JOIN e.registrations r WHERE e.id = :eventId AND r.registrationStatus = 0")
    List <RegistrationEntity> getEventOpenedRegistrations(
            @Param("eventId") Long eventId
    );

    @Transactional
    @Query("""
            SELECT e FROM EventEntity e 
            WHERE (e.locationId = :location_id) 
            AND (e.dateStart < :date_end)
            AND (e.dateEnd > :date_start)
            """)
    List<EventEntity> findEventByDate(
            @Param("date_start") ZonedDateTime start,
            @Param("date_end") ZonedDateTime end,
            @Param("location_id") Long locationId
        );

    @Transactional
    @Query("""
            SELECT e FROM EventEntity e 
            WHERE (:name IS NULL OR e.name LIKE %:name%) 
            AND (:placesMin IS NULL OR e.maxPlaces >= :placesMin) 
            AND (:placesMax IS NULL OR e.maxPlaces <= :placesMax) 
            AND (CAST(:dateStartAfter as date) IS NULL OR e.dateStart >= :dateStartAfter) 
            AND (CAST(:dateStartBefore as date) IS NULL OR e.dateStart <= :dateStartBefore) 
            AND (:costMin IS NULL OR e.cost >= :costMin) 
            AND (:costMax IS NULL OR e.cost <= :costMax) 
            AND (:durationMin IS NULL OR e.duration >= :durationMin) 
            AND (:durationMax IS NULL OR e.duration <= :durationMax) 
            AND (:locationId IS NULL OR e.locationId = :locationId) 
            AND (:eventStatus IS NULL OR e.status = :eventStatus)
            """)
    List<EventEntity> findEvents(
            @Param("name") String name,
            @Param("placesMin") Long placesMin,
            @Param("placesMax") Long placesMax,
            @Param("dateStartAfter") ZonedDateTime dateStartAfter,
            @Param("dateStartBefore") ZonedDateTime dateStartBefore,
            @Param("costMin") BigDecimal costMin,
            @Param("costMax") BigDecimal costMax,
            @Param("durationMin") Integer durationMin,
            @Param("durationMax") Integer durationMax,
            @Param("locationId") Long locationId,
            @Param("eventStatus") EventStatus eventStatus
    );
}
