package ru.event.manager.eventmanager.location.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@RequestMapping("/api/location-service")
public class LocationService {

    @GetMapping("/locations")
    public void getAllLocations(){

    }

    @PostMapping("/locations")
    public void addLocation(){

    }

    @DeleteMapping("/locations/{locationId}")
    public void deleteLocation(@PathVariable("locationId") Long locationId){

    }

    @GetMapping("/locations/{locationId}")
    public void getLocationById(@PathVariable("locationId") Long locationId){

    }

    @PutMapping("/locations/{locationId}")
    public void updateLocation(@PathVariable("locationId") Long locationId){

    }
}
