package org.example.eventmanager.events.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.domain.EventStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@Slf4j
@AllArgsConstructor
public class EventSchedulerConfig {
    private final EventSchedulerService eventSchedulerService;

    @Scheduled(fixedRate = 60000)
    public void scheduleCheckWaitingEvents() throws ParseException {
        eventSchedulerService.scheduleCheckEventStatus(EventStatus.WAITING);
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleCheckStartedEvents() throws ParseException {
        eventSchedulerService.scheduleCheckEventStatus(EventStatus.STARTED);
    }
}
