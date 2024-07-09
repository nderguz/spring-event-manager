package org.example.eventmanager.location;

import lombok.AllArgsConstructor;
import org.example.eventmanager.location.entities.LocationEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationEntity> getLocations() {
        return locationRepository.findAll();
    }

    public LocationEntity createLocation(LocationEntity locationEntity) {
        locationRepository.save(locationEntity);
        return locationEntity;
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    public LocationEntity getLocationById(Long locationId) {
        return locationRepository.getReferenceById(locationId);
    }

    public LocationEntity updateLocation(Long locationId,LocationEntity locationEntity) {
        locationRepository.deleteById(locationId);
        locationRepository.save(locationEntity);
        return locationEntity;
    }
}
