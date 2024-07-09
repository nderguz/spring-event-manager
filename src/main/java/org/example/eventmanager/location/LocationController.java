package org.example.eventmanager.location;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.eventmanager.location.entities.EntityConverter;
import org.example.eventmanager.location.entities.LocationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final EntityConverter entityConverter;

    @GetMapping("/locations")
    public ResponseEntity<List<LocationDto>> getAllLocations(){
        var locations = locationService.getLocations().stream().map(entityConverter::toDto).toList();
        return ResponseEntity.ok(locations);
    }

    @PostMapping("/locations")
    public ResponseEntity<LocationDto> createLocation(@Valid @RequestBody LocationDto locationDto){
        var location = locationService.createLocation(entityConverter.toEntity(locationDto));
        return ResponseEntity.ok(entityConverter.toDto(location));
    }

    @DeleteMapping("/locations/{locationId}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long locationId){
        locationService.deleteLocation(locationId);
        return ResponseEntity.ok("Deleted location with id " + locationId);
    }

    @GetMapping("/locations/{locationId}")
    public ResponseEntity<LocationDto> getLocation(@PathVariable Long locationId){
        var foundLocation = locationService.getLocationById(locationId);
        return ResponseEntity.ok(entityConverter.toDto(foundLocation));
    }

    @PutMapping("/locations/{locationId}")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable Long locationId, @Valid @RequestBody LocationDto locationDto){
        locationService.updateLocation(locationId, entityConverter.toEntity(locationDto));
        return ResponseEntity.ok(entityConverter.toDto(locationService.getLocationById(locationId)));
    }
}
