package ru.event.manager.eventmanager.location.entity;

public record Location (
        Long id,
        String name,
        String address,
        int capacity,
        String description
){
}
