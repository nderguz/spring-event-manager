package org.example.eventmanager.location.entities;

// Бизнес сущность
public record Location (
        Long Id,

//        @NotBlank(message = "Location name cannot be empty")
        String name,

//        @NotBlank(message = "Location address cannot be empty")
        String address,

//        @Min(value = 1, message = "Capacity must be greater than 0")
        Long capacity,

        String description
){

}
