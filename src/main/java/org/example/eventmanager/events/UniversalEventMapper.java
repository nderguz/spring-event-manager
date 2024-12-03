package org.example.eventmanager.events;

import org.example.eventmanager.events.api.EventDto;
import org.example.eventmanager.events.api.RequestEvent;
import org.example.eventmanager.events.db.EventEntity;
import org.example.eventmanager.events.domain.EventDomain;
import org.example.eventmanager.events.domain.Registration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UniversalEventMapper {

    public EventDto domainToDto(EventDomain entity) {
        return new EventDto(
                entity.occupiedPlaces().size(),
                entity.date(),
                entity.duration(),
                entity.cost(),
                entity.maxPlaces(),
                entity.locationId(),
                entity.name(),
                entity.id(),
                entity.ownerId(),
                entity.status()
        );
    }

    public EventDomain entityToDomain(EventEntity entity) {
        List<Registration> occupiedPlaces = entity.getRegistrations().stream()
                .map(it -> new Registration(
                        it.getRegistrationId(),
                        it.getUserId(),
                        entity.getId()
                )).toList();

        return new EventDomain(
                occupiedPlaces,
                entity.getDateStart(),
                entity.getDuration(),
                entity.getCost(),
                entity.getMaxPlaces(),
                entity.getLocation().getId(),
                entity.getName(),
                entity.getId(),
                entity.getOwner().getId(),
                entity.getStatus()
        );
    }
}
