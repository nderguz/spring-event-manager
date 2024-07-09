package org.example.eventmanager.location.entities;

import org.springframework.stereotype.Component;

@Component
public class EntityConverter {
    public LocationDto toDto(LocationEntity entity) {
        return new LocationDto(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getCapacity(),
                entity.getDescription()
        );
    }

    public LocationEntity toEntity(LocationDto dto) {
        return new LocationEntity(
                dto.Id(),
                dto.name(),
                dto.address(),
                dto.capacity(),
                dto.description()
        );
    }
}
