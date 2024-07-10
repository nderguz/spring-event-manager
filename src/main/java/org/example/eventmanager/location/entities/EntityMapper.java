package org.example.eventmanager.location.entities;

import org.springframework.stereotype.Component;

@Component
public class EntityMapper {
    public LocationEntity toEntity(Location locationDomain) {
        return new LocationEntity(
                locationDomain.Id(),
                locationDomain.name(),
                locationDomain.address(),
                locationDomain.capacity(),
                locationDomain.description()
        );
    }

    public Location toDomain(LocationEntity locationEntity) {
        return new Location(
                locationEntity.getId(),
                locationEntity.getName(),
                locationEntity.getAddress(),
                locationEntity.getCapacity(),
                locationEntity.getDescription()
        );
    }
}
