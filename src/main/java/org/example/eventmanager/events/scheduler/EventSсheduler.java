package org.example.eventmanager.events.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.UniversalEventMapper;
import org.example.eventmanager.events.db.EventEntity;
import org.example.eventmanager.events.db.EventRepository;
import org.example.eventmanager.events.domain.EventStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class EventSсheduler implements EventSchedulerService {

    private final EventRepository eventRepository;
    private final UniversalEventMapper universalEventMapper;

    //Проверка начала мероприятия
    //Отправка нотификации о начале мероприятия
    @Override
    public void scheduleCheckEventStatus(EventStatus eventStatus) {
        ZonedDateTime dateTime = ZonedDateTime.now();
        List<EventEntity> events = eventRepository.findAllByStatus(eventStatus);
        for (EventEntity event : events) {
            var date = event.getDateStart();
            if (Objects.equals(event.getStatus(), EventStatus.WAITING)) {
                if (dateTime.isAfter(date)) {
//                    var eventToKafka = universalEventMapper.entityToDomain(event);
                    event.setStatus(EventStatus.STARTED);
                    eventRepository.save(event);
                }
            } else if (Objects.equals(event.getStatus(), EventStatus.STARTED)) {
                if (dateTime.isAfter(event.getDateEnd())) {
//                    var eventToKafka = universalEventMapper.entityToDomain(event);
                    event.setStatus(EventStatus.FINISHED);
                    eventRepository.save(event);
                }
            }

        }
    }
}
