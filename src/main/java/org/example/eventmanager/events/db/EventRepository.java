package org.example.eventmanager.events.db;

import org.example.eventmanager.events.db.model.EventEntity;
import org.example.eventmanager.events.db.model.RegistrationEntity;
import org.example.eventmanager.events.domain.model.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    @Query("SELECT e FROM EventEntity e WHERE e.status = :status")
    List<EventEntity> findAllByStatus(@Param("status") EventStatus status);

    @Query("SELECT e FROM EventEntity e WHERE e.owner.id = :user_id")
    List<EventEntity> findAllUserEvents(@Param("user_id") Long userId);

    @Modifying
    @Query("UPDATE EventEntity e SET e.status = :status where e.id = :id")
    void changeEventStatus(
            @Param("id") Long eventId,
            @Param("status") EventStatus eventStatus
    );


    @Query("SELECT e.registrations FROM EventEntity e JOIN e.registrations r WHERE e.id = :eventId AND r.registrationStatus = \"OPENED\"")
    List<RegistrationEntity> getEventOpenedRegistrations(
            @Param("eventId") Long eventId
    );

    @Query("""
            SELECT count(e)
            FROM EventEntity e
            WHERE (e.location.Id = :location_id)
            AND (e.dateStart < :date_end)
            AND (e.dateEnd > :date_start)
            AND (e.status != :event_status)
            """)
    Integer checkIfLocationDateReserved(
            @Param("date_start") LocalDateTime start,
            @Param("date_end") LocalDateTime end,
            @Param("location_id") Long locationId,
            @Param("event_status") EventStatus eventStatus
        );

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
            AND (:locationId IS NULL OR e.location.Id = :locationId)
            AND (:eventStatus IS NULL OR e.status = :eventStatus)
            """)
    List<EventEntity> findEvents(
            @Param("name") String name,
            @Param("placesMin") Long placesMin,
            @Param("placesMax") Long placesMax,
            @Param("dateStartAfter") LocalDateTime dateStartAfter,
            @Param("dateStartBefore") LocalDateTime dateStartBefore,
            @Param("costMin") BigDecimal costMin,
            @Param("costMax") BigDecimal costMax,
            @Param("durationMin") Integer durationMin,
            @Param("durationMax") Integer durationMax,
            @Param("locationId") Long locationId,
            @Param("eventStatus") EventStatus eventStatus
    );
}
