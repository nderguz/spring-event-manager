package org.example.eventmanager.events;

import org.example.eventmanager.events.api.model.EventFullInfo;
import org.example.eventmanager.events.db.model.EventEntity;
import org.example.eventmanager.events.domain.model.EventDomain;
import org.example.eventmanager.events.domain.model.Registration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UniversalEventMapper {

    public EventFullInfo domainToDto(EventDomain entity) {
        return new EventFullInfo(
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
