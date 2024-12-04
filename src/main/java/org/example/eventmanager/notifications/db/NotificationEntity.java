package org.example.eventmanager.notifications.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.eventmanager.events.db.EventEntity;
import org.example.eventmanager.notifications.NotificationStatus;
import org.example.eventmanager.users.db.UserEntity;

import java.time.ZonedDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Column(name = "date")
    private ZonedDateTime date;
}
