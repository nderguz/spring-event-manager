package org.example.eventmanager.events.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.domain.model.EventStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventSchedulerConfig {
    private final EventSchedulerService eventSchedulerService;

    @Scheduled(cron = "0 * * * * *")
    public void scheduleCheckWaitingEvents() throws ParseException {
        eventSchedulerService.scheduleCheckEventStatus(EventStatus.WAITING);
    }

    @Scheduled(cron = "0 * * * * *")
    public void scheduleCheckStartedEvents() throws ParseException {
        eventSchedulerService.scheduleCheckEventStatus(EventStatus.STARTED);
    }
}
