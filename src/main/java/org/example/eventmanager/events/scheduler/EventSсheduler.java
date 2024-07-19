package org.example.eventmanager.events.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.model.EventEntity;
import org.example.eventmanager.events.model.EventStatus;
import org.example.eventmanager.events.repository.EventRepository;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class EventSсheduler implements EventSchedulerService{

    private final EventRepository eventRepository;

    @Override
    public void scheduleCheckWaitingEvents() {
        Instant dateTime = Instant.now();
        List<EventEntity> events = eventRepository.findAllByStatus(EventStatus.WAITING.toString());
        for(EventEntity event : events){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try{
                Instant eventDate = formatter.parse(event.getDate().toString()).toInstant();
                if(dateTime.isAfter(eventDate)){
                    event.setStatus(EventStatus.STARTED.toString());
                    eventRepository.save(event);
                }
            }catch (ParseException e){
                log.error(e.getMessage());
            }
        }

    }

    @Override
    public void scheduleCheckStartedEvents(){
        Instant dateTime = Instant.now();
        List<EventEntity> events = eventRepository.findAllByStatus(EventStatus.STARTED.toString());
        for(EventEntity event : events){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            var duration = event.getDuration() * 60000L;
            try{
                Instant eventEndDate = formatter.parse(event.getDate().toString()).toInstant().plusMillis(duration);
                if(dateTime.isAfter(eventEndDate)){
                    event.setStatus(EventStatus.FINISHED.toString());
                    eventRepository.save(event);
                }
            }catch (ParseException e){
                log.error(e.getMessage());
            }
        }
    }
}
