package org.example.eventmanager.notifications.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.notifications.db.NotificationEntity;
import org.example.eventmanager.notifications.db.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler implements NotificationSchedulerService {

    private final NotificationRepository notificationRepository;

    @Override
    public void deleteExpiredNotifications() {
        ZonedDateTime timeNow = ZonedDateTime.now();
        ZonedDateTime expiryTime = timeNow.minusDays(7);
        Optional<List<NotificationEntity>> events = notificationRepository.findExpiredNotifications(expiryTime);
        if(events.isEmpty()) {
            return;
        }
        log.info("Found expired notifications");
        notificationRepository.deleteAll(events.get());
    }
}
