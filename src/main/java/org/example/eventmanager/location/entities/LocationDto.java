package org.example.eventmanager.location.entities;

public record LocationDto(
        Long Id,
        String name,
        String address,
        int capacity,
        String description
) {
}
