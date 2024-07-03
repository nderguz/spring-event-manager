package ru.event.manager.eventmanager.location;

import org.springframework.stereotype.Component;
import ru.event.manager.eventmanager.location.dto.LocationDto;
import ru.event.manager.eventmanager.location.entity.Location;

@Component
public class LocationEntityConverter {

    public Location toEntity(LocationDto locationDto){
        return new Location(
                locationDto.id(),
                locationDto.name(),
                locationDto.address(),
                locationDto.capacity(),
                locationDto.description()
        );
    }

    public LocationDto toDto(Location location){
        return new LocationDto(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

}
