package org.example.eventmanager.location.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.location.domain.LocationService;
import org.example.eventmanager.location.UniversalLocationMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/locations")
@Slf4j
public class LocationController {

    private final LocationService locationService;
    private final UniversalLocationMapper universalLocationMapper;

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations(){
        var locationsList = locationService.getLocations().stream().map(universalLocationMapper::domainToDto).toList();
        return ResponseEntity.ok(locationsList);
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(
            @RequestBody @Valid LocationDto locationDto
    ){
        var createdLocation = locationService.createLocation(universalLocationMapper.dtoToDomain(locationDto));
        return ResponseEntity.status(201)
                .body(universalLocationMapper.domainToDto(createdLocation));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<LocationDto> deleteLocation(
            @PathVariable Long locationId
    ){
        var deletedLocation = locationService.deleteLocation(locationId);
        return ResponseEntity.status(204).body(universalLocationMapper.domainToDto(deletedLocation));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocation(
            @PathVariable Long locationId
    ){
        var foundLocation = universalLocationMapper.domainToDto(locationService.getLocationById(locationId));
        return ResponseEntity.ok(foundLocation);
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable Long locationId,
            @RequestBody @Valid  LocationDto locationDto
    ){
        var updatedLocation = locationService.updateLocation(locationId, universalLocationMapper.dtoToDomain(locationDto));
        return ResponseEntity.ok(universalLocationMapper.domainToDto(updatedLocation));
    }
}
