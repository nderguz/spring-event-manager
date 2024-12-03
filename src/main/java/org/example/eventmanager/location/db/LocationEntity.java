package org.example.eventmanager.location.db;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "name",
            nullable = false
    )
    private String name;

    @Column(nullable = false,
            name = "address")
    private String address;

    @Column(name = "capacity")
    private Long capacity;

    @Column(name = "description")
    private String description;
}
