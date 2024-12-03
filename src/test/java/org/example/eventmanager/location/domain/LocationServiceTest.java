package org.example.eventmanager.location.domain;

import jakarta.persistence.EntityNotFoundException;
import org.example.eventmanager.location.UniversalLocationMapper;
import org.example.eventmanager.location.db.LocationEntity;
import org.example.eventmanager.location.db.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private UniversalLocationMapper universalLocationMapper;

    @InjectMocks
    private LocationService locationService;

    private Location location;
    private LocationEntity locationEntity;

    @BeforeEach
    public void setUp() {
        location = new Location(
                1L,
                "Test Location",
                "Test Address",
                100L,
                "Test Description");
        locationEntity = new LocationEntity(1L,
                "Test Location",
                "Test Address",
                100L,
                "Test Description");
    }

    @Test
    public void testGetLocations() {
        when(locationRepository.findAll()).thenReturn(List.of(locationEntity));
        when(universalLocationMapper.entityToDomain(locationEntity)).thenReturn(location);

        List<Location> locations = locationService.getLocations();

        assertNotNull(locations);
        assertEquals(1, locations.size());
        assertEquals("Test Location", locations.get(0).name());
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    public void testCreateLocation() {
        Location newLocation = new Location(
                null,
                "New Location",
                "New Address",
                200L,
                "New Description"
        );
        LocationEntity newLocationEntity = new LocationEntity(
                null,
                "New Location",
                "New Address",
                200L,
                "New Description"
        );
        LocationEntity savedLocationEntity = new LocationEntity(
                2L,
                "New Location",
                "New Address",
                200L,
                "New Description"
        );
        Location savedLocation = new Location(
                2L,
                "New Location",
                "New Address",
                200L,
                "New Description"
        );

        when(universalLocationMapper.domainToEntity(newLocation)).thenReturn(newLocationEntity);
        when(locationRepository.save(newLocationEntity)).thenReturn(savedLocationEntity);
        when(universalLocationMapper.entityToDomain(savedLocationEntity)).thenReturn(savedLocation);

        Location createdLocation = locationService.createLocation(newLocation);

        assertNotNull(createdLocation);
        assertEquals(2L, createdLocation.Id());
        assertEquals("New Location", createdLocation.name());
        verify(locationRepository, times(1)).save(newLocationEntity);
    }

    @Test
    public void testCreateLocationWithProvidedId() {
        Location newLocation = new Location(
                1L,
                "New Location",
                "New Address",
                200L,
                "New Description"
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            locationService.createLocation(newLocation);
        });

        assertEquals("Can not create location with provided ID.", exception.getMessage());
        verify(locationRepository, never()).save(any());
    }

    @Test
    public void testDeleteLocation() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(locationEntity));
        when(locationService.deleteLocation(1L)).thenReturn(location);

        Location deletedLocation = locationService.deleteLocation(1L);

        assertNotNull(deletedLocation);
        assertEquals(1L, deletedLocation.Id());

        verify(locationRepository, times(1)).findById(1L);
        verify(locationRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteLocationNotFound() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            locationService.deleteLocation(1L);
        });

        assertEquals("Location not found. ID = 1", exception.getMessage());
        verify(locationRepository, times(1)).findById(1L);
        verify(locationRepository, never()).deleteById(1L);
    }

    @Test
    public void testGetLocationById() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(locationEntity));
        when(universalLocationMapper.entityToDomain(locationEntity)).thenReturn(location);

        Location foundLocation = locationService.getLocationById(1L);

        assertNotNull(foundLocation);
        assertEquals(1L, foundLocation.Id());
        assertEquals("Test Location", foundLocation.name());
        verify(locationRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetLocationByIdNotFound() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            locationService.getLocationById(1L);
        });

        assertEquals("Location not found. ID = 1", exception.getMessage());
        verify(locationRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateLocation() {
        Location updatedLocation = new Location(
                null,
                "Updated Location",
                "Updated Address",
                150L,
                "Updated Description"
        );

        when(locationRepository.findById(1L)).thenReturn(Optional.of(locationEntity));
        when(locationRepository.save(locationEntity)).thenReturn(locationEntity);
        when(universalLocationMapper.entityToDomain(locationEntity)).thenReturn(updatedLocation);

        Location result = locationService.updateLocation(1L, updatedLocation);

        assertNotNull(result);
        assertEquals("Updated Location", result.name());
        verify(locationRepository, times(1)).findById(1L);
        verify(locationRepository, times(1)).save(locationEntity);
    }

    @Test
    public void testUpdateLocationWithId() {
        Location updatedLocation = new Location(
                1L,
                "Updated Location",
                "Updated Address",
                150L,
                "Updated Description"
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            locationService.updateLocation(1L, updatedLocation);
        });

        assertEquals("Can not update location with provided ID.", exception.getMessage());
        verify(locationRepository, never()).save(any());
    }

    @Test
    public void testUpdateLocationNotFound() {
        Location updatedLocation = new Location(
                null,
                "Updated Location",
                "Updated Address",
                150L,
                "Updated Description"
        );

        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            locationService.updateLocation(1L, updatedLocation);
        });

        assertEquals("Location not found. ID = 1", exception.getMessage());
        verify(locationRepository, times(1)).findById(1L);
        verify(locationRepository, never()).save(any());
    }
}