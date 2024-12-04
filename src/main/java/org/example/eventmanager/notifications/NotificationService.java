package org.example.eventmanager.notifications;

import lombok.RequiredArgsConstructor;
import org.example.eventmanager.kafka.KafkaSender;
import org.example.eventmanager.notifications.db.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final KafkaSender kafkaSender;

    public void statusChangeNotification() {
        var newNotifications = notificationRepository.findNewNotifications(NotificationStatus.NEW);
        if (newNotifications.isEmpty()) {
            return;
        }
    }

    public void eventInfoChangeNotification() {

    }
}
