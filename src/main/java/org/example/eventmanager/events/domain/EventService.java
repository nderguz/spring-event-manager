package org.example.eventmanager.events.domain;

import lombok.AllArgsConstructor;
import org.example.eventmanager.events.api.EventCreateRequestDto;
import org.example.eventmanager.events.db.EventRepository;
import org.example.eventmanager.events.db.RegistrationRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class EventService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;

    public Event createEvent(EventCreateRequestDto eventCreateRequestDto) {
        return new Event(
                1L,
                "1",
                1L,
                2,
                new ArrayList<>(),
                null,
                15,
                16,
                17L,
                EventStatus.CANCELLED
        );
    }
}
