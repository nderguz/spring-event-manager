package org.example.eventmanager.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.api.UpdateEvent;
import org.example.eventmanager.events.domain.EventDomain;
import org.example.eventmanager.events.domain.EventStatus;
import org.example.eventmanager.users.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, EventChangeKafkaMessage> kafkaTemplate;

    private final UserRepository userRepository;

    public void sendMessage(String topic, EventChangeKafkaMessage message) {
        kafkaTemplate.send(topic, message);
        log.info("Message sent to topic " + topic + " : " + message);

    }

    public void sendMessageToKafka(EventDomain oldEvent,
                                    UpdateEvent newEvent,
                                    EventStatus status
    ){
        var listOfUsers = userRepository.findAllRegisteredUsersIdToEvent(oldEvent.id())
                .orElse(null);
        var name = new EventFieldChange<>(oldEvent.name(), newEvent.name());
        var maxPlaces = new EventFieldChange<>(oldEvent.maxPlaces(), newEvent.maxPlaces());
        var date = new EventFieldChange<>(oldEvent.date(), newEvent.date());
        var cost = new EventFieldChange<>(oldEvent.cost(), newEvent.cost());
        var duration = new EventFieldChange<>(oldEvent.duration(), newEvent.duration());
        var locationId = new EventFieldChange<>(oldEvent.locationId(), newEvent.locationId());
        var newStatus = new EventFieldChange<>(oldEvent.status(), status);

        var messageToSend = new EventChangeKafkaMessage(
                oldEvent.id(),
                listOfUsers,
                oldEvent.ownerId(),
                name,
                maxPlaces,
                date,
                cost,
                duration,
                locationId,
                newStatus
        );
        sendMessage("events", messageToSend);
    }

    public void sendMessageToKafka(EventDomain event,
                                    EventStatus status
    ){
        var listOfUsers = userRepository.findAllRegisteredUsersIdToEvent(event.id())
                .orElse(null);

        var name = new EventFieldChange<>(event.name(), event.name());
        var maxPlaces = new EventFieldChange<>(event.maxPlaces(), event.maxPlaces());
        var date = new EventFieldChange<>(event.date(), event.date());
        var cost = new EventFieldChange<>(event.cost(), event.cost());
        var duration = new EventFieldChange<>(event.duration(), event.duration());
        var locationId = new EventFieldChange<>(event.locationId(), event.locationId());
        var newStatus = new EventFieldChange<>(event.status(), status);

        var messageToSend = new EventChangeKafkaMessage(
                event.id(),
                listOfUsers,
                event.ownerId(),
                name,
                maxPlaces,
                date,
                cost,
                duration,
                locationId,
                newStatus
        );
        sendMessage("events", messageToSend);
    }
}
