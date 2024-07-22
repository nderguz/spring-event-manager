package org.example.eventmanager.location;

import org.example.eventmanager.location.api.LocationDto;
import org.example.eventmanager.location.db.LocationEntity;
import org.example.eventmanager.location.domain.Location;
import org.springframework.stereotype.Component;

@Component
public class UniversalLocationMapper {
    public LocationDto domainToDto(Location locationDomain) {
        return new LocationDto(
                locationDomain.Id(),
                locationDomain.name(),
                locationDomain.address(),
                locationDomain.capacity(),
                locationDomain.description()
        );
    }

    public Location dtoToDomain(LocationDto dto) {
        return new Location(
                dto.Id(),
                dto.name(),
                dto.address(),
                dto.capacity(),
                dto.description()
        );
    }
    public LocationEntity domainToEntity(Location locationDomain) {
        return new LocationEntity(
                locationDomain.Id(),
                locationDomain.name(),
                locationDomain.address(),
                locationDomain.capacity(),
                locationDomain.description()
        );
    }

    public Location entityToDomain(LocationEntity locationEntity) {
        return new Location(
                locationEntity.getId(),
                locationEntity.getName(),
                locationEntity.getAddress(),
                locationEntity.getCapacity(),
                locationEntity.getDescription()
        );
    }
}
