package org.example.eventmanager.location.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "locations")
public class LocationEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "location_id")
    private Long Id;

    @Column(name = "location_name",
            nullable = false
    )
    private String name;

    @Column(nullable = false,
            name = "location_adress")
    private String address;

    @Column(name = "capacity")
    private Long capacity;

    @Column(name = "description")
    private String description;

}
