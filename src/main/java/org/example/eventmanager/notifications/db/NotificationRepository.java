package org.example.eventmanager.notifications.db;

import org.example.eventmanager.notifications.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("""
    SELECT n from NotificationEntity n
    WHERE n.date >= :time
""")
    Optional<List<NotificationEntity>> findExpiredNotifications(
            @Param("time") ZonedDateTime time
    );

    @Query("""
    SELECT n from NotificationEntity n
    WHERE n.status = :status
""")
    Optional<List<NotificationEntity>> findNewNotifications(
            @Param("status") NotificationStatus status
    );
}
