package org.example.eventmanager.location;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.eventmanager.location.entities.EntityConverter;
import org.example.eventmanager.location.entities.LocationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final EntityConverter entityConverter;
    private final static Logger log = LoggerFactory.getLogger(LocationController.class);

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations(){
        log.info("Get request for all locations");
        var locations = locationService.getLocations().stream().map(entityConverter::toDto).toList();
        return ResponseEntity.ok(locations);
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@Valid @RequestBody LocationDto locationDto){
        log.info("Get request for location create: locationDto={}", locationDto);
        var location = locationService.createLocation(entityConverter.toEntity(locationDto));
        return ResponseEntity.ok(entityConverter.toDto(location));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long locationId){
        log.info("Get request for location delete: locationId={}", locationId);
        locationService.deleteLocation(locationId);
        return ResponseEntity.ok("Deleted location with id " + locationId);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocation(@PathVariable Long locationId){
        log.info("Get request for location by id: locationId={}", locationId);
        var foundLocation = locationService.getLocationById(locationId);
        return ResponseEntity.ok(entityConverter.toDto(foundLocation));
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable Long locationId, @Valid @RequestBody LocationDto locationDto){
        log.info("Get request for location change: locationId={}, to {}", locationId, locationDto);
        locationService.updateLocation(locationId, entityConverter.toEntity(locationDto));
        return ResponseEntity.ok(entityConverter.toDto(locationService.getLocationById(locationId)));
    }
}
