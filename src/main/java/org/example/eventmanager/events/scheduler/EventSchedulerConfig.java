package org.example.eventmanager.events.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class EventSchedulerConfig {
    private final EventSchedulerService eventSchedulerService;

    @Scheduled(fixedRate = 60000)
    public void scheduleCheckWaitingEvents(){
        eventSchedulerService.scheduleCheckWaitingEvents();
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleCheckStartedEvents(){
        eventSchedulerService.scheduleCheckStartedEvents();
    }

}
