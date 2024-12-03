package org.example.eventmanager.location.domain;

import lombok.Builder;


@Builder
public record Location (
        Long Id,
        String name,
        String address,
        Long capacity,
        String description
){

}
