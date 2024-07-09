package org.example.eventmanager.location.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "locations",
        uniqueConstraints = @UniqueConstraint(
                name = "location_unique",
                columnNames = "location_name"
        )
)
public class LocationEntity {
    @Id
    @SequenceGenerator(
            name = "location_sequence",
            sequenceName = "location_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "location_sequence"
    )
    private Long Id;
    @Column(name = "location_name",
            nullable = false
    )
    private String name;
    @Column(nullable = false,
            name = "location_adress")
    private String address;
    private Long capacity;
    private String description;

}
