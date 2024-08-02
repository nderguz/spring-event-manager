package org.example.eventmanager.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.eventmanager.events.domain.EventStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@AllArgsConstructor
@Getter
@Setter
public class EventChangeKafkaMessage {

    private Long eventId;

    private final List<Long> users;
    private final Long ownerId;
    private final Long changedById;

    private EventFieldChange<String> name;
    private EventFieldChange<Integer> maxPlaces;
    private EventFieldChange<ZonedDateTime> date;
    private EventFieldChange<BigDecimal> cost;
    private EventFieldChange<Integer> duration;
    private EventFieldChange<Long> locationId;
    private EventFieldChange<EventStatus> status;
}
