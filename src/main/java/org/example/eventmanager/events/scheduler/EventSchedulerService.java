package org.example.eventmanager.events.scheduler;

import org.example.eventmanager.events.domain.model.EventStatus;

import java.text.ParseException;

public interface EventSchedulerService {
    void scheduleCheckEventStatus(EventStatus eventStatus) throws ParseException;
}
