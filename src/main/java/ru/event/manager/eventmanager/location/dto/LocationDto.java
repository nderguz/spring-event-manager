package ru.event.manager.eventmanager.location.dto;

public record LocationDto (
    Long id,
    String name,
    String address,
    int capacity,
    String description
) {
}
