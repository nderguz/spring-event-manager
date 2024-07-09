package org.example.eventmanager.location.repository;

import org.example.eventmanager.location.LocationRepository;
import org.example.eventmanager.location.entities.LocationEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LocationRepositoryTest {
    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void createLocation(){
        LocationEntity location = LocationEntity.builder()
                .name("Moscow")
                .address("Profa")
                .capacity(15)
                .description("Work")
                .build();
        locationRepository.save(location);
    }
}