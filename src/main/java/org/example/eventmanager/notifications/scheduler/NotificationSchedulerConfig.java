package org.example.eventmanager.notifications.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationSchedulerConfig {
    private final NotificationScheduler notificationScheduler;

    @Scheduled(cron = "0 * * * * *")
    public void deleteExpiredNotifications() throws ParseException {
        notificationScheduler.deleteExpiredNotifications();
    }
}
