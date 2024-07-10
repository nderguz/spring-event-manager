package org.example.eventmanager.location;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.eventmanager.location.entities.EntityMapper;
import org.example.eventmanager.location.entities.Location;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final EntityMapper entityMapper;

    public List<Location> getLocations() {
        return locationRepository.findAll().stream()
                .map(entityMapper::toDomain)
                .toList();
    }

    public Location createLocation(Location locationToCreate) {
        if(locationToCreate.Id() != null){
            throw new IllegalArgumentException("Can not create location with provided ID.");
        }
        var createdLocation = locationRepository.save(entityMapper.toEntity(locationToCreate));
        return entityMapper.toDomain(createdLocation);
    }

    public Location deleteLocation(Long locationId) {
        var entityToDelete = locationRepository.findById(locationId).orElseThrow(() -> new EntityNotFoundException("Location not found. ID = %s"
                .formatted(locationId)));
        locationRepository.deleteById(locationId);
        return entityMapper.toDomain(entityToDelete);
    }

    public Location getLocationById(Long locationId) {
        var foundEntityById = locationRepository.findById(locationId).orElseThrow(() -> new EntityNotFoundException("Location not found. ID = %s"
                .formatted(locationId)));
        return entityMapper.toDomain(foundEntityById);
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
        return entityMapper.toDomain(updatedLocation);
    }
}
