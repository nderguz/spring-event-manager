package org.example.eventmanager.location;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.eventmanager.location.entities.LocationDto;
import org.example.eventmanager.location.entities.UniversalLocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final UniversalLocationMapper universalLocationMapper;
    private final static Logger log = LoggerFactory.getLogger(LocationController.class);

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations(){
        log.info("Get request for all locations");
        var locationsList = locationService.getLocations().stream().map(universalLocationMapper::domainToDto).toList();
        return ResponseEntity.ok(locationsList);
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(
            @RequestBody @Valid LocationDto locationDto
    ){
        log.info("Get request for location create: locationDto={}", locationDto);
        var createdLocation = locationService.createLocation(universalLocationMapper.dtoToDomain(locationDto));
        return ResponseEntity.status(201)
                .body(universalLocationMapper.domainToDto(createdLocation));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<LocationDto> deleteLocation(
            @PathVariable Long locationId
    ){
        log.info("Get request for location delete: locationId={}", locationId);
        var deletedLocation = locationService.deleteLocation(locationId);
        return ResponseEntity.status(204).body(universalLocationMapper.domainToDto(deletedLocation));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocation(
            @PathVariable Long locationId
    ){
        log.info("Get request for location by id: locationId={}", locationId);
        var foundLocation = universalLocationMapper.domainToDto(locationService.getLocationById(locationId));
        return ResponseEntity.ok(foundLocation);
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable Long locationId,
            @RequestBody @Valid  LocationDto locationDto
    ){
        log.info("Get request for location change: locationId={}, to {}", locationId, locationDto);
        var updatedLocation = locationService.updateLocation(locationId, universalLocationMapper.dtoToDomain(locationDto));
        return ResponseEntity.ok(universalLocationMapper.domainToDto(updatedLocation));
    }
}
