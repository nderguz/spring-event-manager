package org.example.eventmanager.events.model.registration;

import org.example.eventmanager.events.model.RegistrationStatus;

public record RegistrationDomain(
        Long registrationId,
        Long eventId,
        Long userId,
        RegistrationStatus registrationStatus
){
}
