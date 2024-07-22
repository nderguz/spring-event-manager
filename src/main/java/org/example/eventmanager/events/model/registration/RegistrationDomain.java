package org.example.eventmanager.events.model.registration;

public record RegistrationDomain(
        Long registrationId,
        Long eventId,
        Long userId,
        String registrationStatus
){
}
