package org.example.eventmanager.location;

import org.example.eventmanager.location.db.LocationEntity;
import org.example.eventmanager.location.api.Location;
import org.springframework.stereotype.Component;

@Component
public class UniversalLocationMapper {

    public LocationEntity buildEntity(Location location){
        return LocationEntity.builder()
                .address(location.address())
                .name(location.name())
                .description(location.description())
                .capacity(location.capacity())
                .build();
    }
    public Location buildResponse(LocationEntity entity){
        return Location.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .description(entity.getDescription())
                .capacity(entity.getCapacity())
                .build();
    }
}
