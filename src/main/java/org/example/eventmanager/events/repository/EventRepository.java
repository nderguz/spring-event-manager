package org.example.eventmanager.events.repository;

import org.example.eventmanager.events.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    @Query("SELECT e FROM EventEntity e WHERE e.status = :status")
    List<EventEntity> findAllByStatus(@Param("status") String status);

    @Query("SELECT e FROM EventEntity e WHERE e.ownerId = :user_id")
    List<EventEntity> findAllUserEvents(@Param("user_id") Long userId);
}
