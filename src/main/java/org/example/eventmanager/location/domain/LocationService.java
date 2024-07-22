package org.example.eventmanager.location.domain;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
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
                .map(universalLocationMapper::entityToDomain)
                .toList();
    }

    public Location createLocation(Location locationToCreate) {
        if(locationToCreate.Id() != null){
            throw new IllegalArgumentException("Can not create location with provided ID.");
        }
        var createdLocation = locationRepository.save(universalLocationMapper.domainToEntity(locationToCreate));
        return universalLocationMapper.entityToDomain(createdLocation);
    }

    public Location deleteLocation(Long locationId) {
        var entityToDelete = locationRepository.findById(locationId).orElseThrow(() -> new EntityNotFoundException("Location not found. ID = %s"
                .formatted(locationId)));
        locationRepository.deleteById(locationId);
        return universalLocationMapper.entityToDomain(entityToDelete);
    }

    public Location getLocationById(Long locationId) {
        var foundEntityById = locationRepository.findById(locationId).orElseThrow(() -> new EntityNotFoundException("Location not found. ID = %s"
                .formatted(locationId)));
        return universalLocationMapper.entityToDomain(foundEntityById);
    }
    public Location updateLocation(Long locationId,Location locationToUpdate) {
        if (locationToUpdate.Id() != null) {
            throw new IllegalArgumentException("Can not update location with provided ID.");
        }
        var entityToUpdate = locationRepository.findById(locationId).orElseThrow(() -> new EntityNotFoundException("Location not found. ID = %s"
                .formatted(locationId)));
        entityToUpdate.setAddress(locationToUpdate.address());
        entityToUpdate.setName(locationToUpdate.name());
        entityToUpdate.setCapacity(locationToUpdate.capacity());
        entityToUpdate.setDescription(locationToUpdate.description());

        var updatedLocation = locationRepository.save(entityToUpdate);
        return universalLocationMapper.entityToDomain(updatedLocation);
    }
}
