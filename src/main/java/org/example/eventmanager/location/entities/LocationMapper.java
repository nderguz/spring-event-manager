package org.example.eventmanager.location.entities;

import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    public LocationDto toDto(Location locationDomain) {
        return new LocationDto(
                locationDomain.Id(),
                locationDomain.name(),
                locationDomain.address(),
                locationDomain.capacity(),
                locationDomain.description()
        );
    }

    public Location toDomain(LocationDto dto) {
        return new Location(
                dto.Id(),
                dto.name(),
                dto.address(),
                dto.capacity(),
                dto.description()
        );
    }
}
