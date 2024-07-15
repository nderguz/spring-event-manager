package org.example.eventmanager.events.db;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "max_places", nullable = false)
    private int maxPlaces;

    @OneToMany(mappedBy = "event")
    private List<RegistrationEntity> registrationList;

//    @Column(name = "date", nullable = false)
//    private LocalDateTime date;

    @Column(name = "cost", nullable = false)
    private int cost;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

//    @Column(name = "status", nullable = false)
//    private EventStatus status;

}
