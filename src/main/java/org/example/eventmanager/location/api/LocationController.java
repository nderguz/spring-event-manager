package org.example.eventmanager.location.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.location.domain.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
@Slf4j
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        return ResponseEntity.ok(locationService.getLocations());
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(
            @RequestBody @Valid Location locationDto
    ) {
        return ResponseEntity.status(201)
                .body(locationService.createLocation(locationDto));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Location> deleteLocation(
            @PathVariable Long locationId
    ) {
        return ResponseEntity.status(204).body(locationService.deleteLocation(locationId));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<Location> getLocation(
            @PathVariable Long locationId
    ) {
        return ResponseEntity.ok(locationService.getLocationById(locationId));
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<Location> updateLocation(
            @PathVariable Long locationId,
            @RequestBody @Valid Location locationDto
    ) {
        return ResponseEntity.ok(locationService.updateLocation(locationId, locationDto));
    }
}
