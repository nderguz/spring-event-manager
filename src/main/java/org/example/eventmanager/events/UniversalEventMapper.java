package org.example.eventmanager.events;

import org.example.eventmanager.events.domain.EventDomain;
import org.example.eventmanager.events.api.EventDto;
import org.example.eventmanager.events.db.EventEntity;
import org.example.eventmanager.events.domain.Registration;
import org.springframework.stereotype.Component;

@Component
public class UniversalEventMapper {

    public EventDto domainToDto(EventDomain entity){
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

    public EventDomain entityToDomain(EventEntity entity){
        return new EventDomain(
                entity.getRegistrations().stream().map(it -> new Registration(
                        it.getRegistrationId(),
                        it.getUserId(),
                        entity.getId()
                )).toList(),
                entity.getDateStart(),
                entity.getDuration(),
                entity.getCost(),
                entity.getMaxPlaces(),
                entity.getLocationId(),
                entity.getName(),
                entity.getId(),
                entity.getOwnerId(),
                entity.getStatus()
        );
    }
}
