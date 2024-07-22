package org.example.eventmanager.events.model;

import org.example.eventmanager.events.model.event.EventDomain;
import org.example.eventmanager.events.model.event.EventDto;
import org.example.eventmanager.events.model.event.EventEntity;
import org.example.eventmanager.events.model.event.Registration;
import org.example.eventmanager.events.model.registration.RegistrationDomain;
import org.example.eventmanager.events.model.registration.RegistrationEntity;
import org.example.eventmanager.location.entities.Location;
import org.example.eventmanager.users.entities.User;
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
                entity.getDate(),
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
