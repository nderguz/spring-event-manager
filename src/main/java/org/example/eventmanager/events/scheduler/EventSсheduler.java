package org.example.eventmanager.events.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.model.event.EventEntity;
import org.example.eventmanager.events.model.EventStatus;
import org.example.eventmanager.events.repository.EventRepository;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class EventSсheduler implements EventSchedulerService {

    private final EventRepository eventRepository;
//todo перепроверить шедулер

    @Override
    public void scheduleCheckEventStatus(EventStatus eventStatus) throws ParseException {
        LocalDateTime dateTime = LocalDateTime.now();
        List<EventEntity> events = eventRepository.findAllByStatus(eventStatus);
        for (EventEntity event : events) {
            var date = event.getDate();

            if (Objects.equals(event.getStatus(), EventStatus.WAITING)) {
                if (dateTime.isAfter(date)) {
                    event.setStatus(EventStatus.STARTED);
                    eventRepository.save(event);
                }
            }
            else if (Objects.equals(event.getStatus(), EventStatus.STARTED)) {
                LocalDateTime eventEndDate = date.plusMinutes(event.getDuration());
                if (dateTime.isAfter(eventEndDate)) {
                    event.setStatus(EventStatus.FINISHED);
                    eventRepository.save(event);
                }
            }

        }
    }
}
