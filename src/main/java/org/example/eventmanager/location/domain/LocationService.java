package org.example.eventmanager.location.domain;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.eventmanager.location.api.Location;
import org.example.eventmanager.location.db.LocationEntity;
import org.example.eventmanager.location.db.LocationRepository;
import org.example.eventmanager.location.UniversalLocationMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final UniversalLocationMapper universalLocationMapper;

    public List<Location> getLocations() {
        return locationRepository.findAll().stream()
                .map(this::buildResponse)
                .toList();
    }

    public Location createLocation(Location locationToCreate) {
        if(locationToCreate.id() != null){
            throw new IllegalArgumentException("Can not create location with provided ID.");
        }
        var createdLocation = locationRepository.save(buildEntity(locationToCreate));
        return buildResponse(createdLocation);
    }

    public Location deleteLocation(Long locationId) {
        var entityToDelete = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found. ID = %s".formatted(locationId)));
        locationRepository.deleteById(locationId);
        return buildResponse(entityToDelete);
    }

    public Location getLocationById(Long locationId) {
        var foundEntityById = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found. ID = %s".formatted(locationId)));
        return buildResponse(foundEntityById);
    }

    public Location updateLocation(Long locationId, Location locationToUpdate) {
        if (locationToUpdate.id() != null) {
            throw new IllegalArgumentException("Can not update location with provided ID.");
        }
        var entityToUpdate = LocationEntity
                .builder()
                    .Id(locationId)
                    .address(locationToUpdate.address())
                    .name(locationToUpdate.name())
                    .capacity(locationToUpdate.capacity())
                    .description(locationToUpdate.description())
                .build();
        var updatedLocation = locationRepository.save(entityToUpdate);
        return buildResponse(updatedLocation);
    }

    private LocationEntity buildEntity(Location location){
        return universalLocationMapper.buildEntity(location);
    }

    private Location buildResponse(LocationEntity entity){
        return universalLocationMapper.buildResponse(entity);
    }
}
