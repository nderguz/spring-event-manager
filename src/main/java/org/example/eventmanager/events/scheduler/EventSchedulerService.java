package org.example.eventmanager.events.scheduler;

public interface EventSchedulerService {
    void scheduleCheckWaitingEvents();
    void scheduleCheckStartedEvents();
}
