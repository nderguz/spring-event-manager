package org.example.eventmanager.events.model;

import org.example.eventmanager.events.model.event.EventDomain;
import org.example.eventmanager.events.model.event.EventDto;
import org.example.eventmanager.events.model.event.EventEntity;
import org.example.eventmanager.events.model.registration.RegistrationDomain;
import org.example.eventmanager.events.model.registration.RegistrationEntity;
import org.example.eventmanager.location.entities.Location;
import org.example.eventmanager.users.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UniversalEventMapper {

    public EventDomain requestToDomain(User user, RequestEvent requestEvent, Location location){
        return new EventDomain(
                1,
                requestEvent.date(),
                requestEvent.duration(),
                requestEvent.cost(),
                requestEvent.maxPlaces(),
                location.Id(),
                requestEvent.name(),
                null,
                user.getId(),
                EventStatus.WAITING
        );
    }

    public EventEntity domainToEntity(EventDomain domain){
        return new EventEntity(
                domain.id(),
                domain.locationId(),
                domain.name(),
                domain.status(),
                domain.ownerId(),
                domain.maxPlaces(),
                domain.cost(),
                domain.duration(),
                domain.date(),
                domain.occupiedPlaces()
        );
    }

    public EventDto domainToDto(EventDomain entity){
        return new EventDto(
                entity.occupiedPlaces(),
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
                entity.getOccupiedPlaces(),
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

    public RegistrationDomain registrationEntityToDomain(RegistrationEntity entity){
        return new RegistrationDomain(
                entity.getRegistrationId(),
                entity.getEventId(),
                entity.getUserId(),
                entity.getRegistrationStatus()
        );
    }
}
