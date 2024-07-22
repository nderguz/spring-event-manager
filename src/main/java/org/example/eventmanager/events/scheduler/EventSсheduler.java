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
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class EventSсheduler implements EventSchedulerService {

    private final EventRepository eventRepository;

    @Override
    public void scheduleCheckEventStatus(EventStatus eventStatus) throws ParseException {
        Instant dateTime = Instant.now();
        List<EventEntity> events = eventRepository.findAllByStatus(eventStatus.toString());
        for (EventEntity event : events) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Instant eventDate = formatter.parse(event.getDate().toString()).toInstant();


            if (Objects.equals(event.getStatus(), EventStatus.WAITING.toString())) {
                if (dateTime.isAfter(eventDate)) {
                    event.setStatus(EventStatus.STARTED.toString());
                    eventRepository.save(event);
                }
            }
            else if (Objects.equals(event.getStatus(), EventStatus.STARTED.toString())) {
                var duration = event.getDuration() * 60000L;
                Instant eventEndDate = eventDate.plusMillis(duration);
                if (dateTime.isAfter(eventEndDate)) {
                    event.setStatus(EventStatus.FINISHED.toString());
                    eventRepository.save(event);

                }
            }

        }
    }
}
