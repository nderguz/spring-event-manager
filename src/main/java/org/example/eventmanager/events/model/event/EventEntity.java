package org.example.eventmanager.events.model.event;


import jakarta.persistence.*;
import lombok.*;
import org.example.eventmanager.events.model.EventStatus;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@Table(name = "events")
public class EventEntity {

    //todo запилить отношения

//
//    @OneToMany(mappedBy = "event")
//    private List<EventRegistrationEntity> registrationList;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private EventStatus status;

    @Column(name = "user_id")
    private Long ownerId;

    @Column(name = "max_places")
    private Integer maxPlaces;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "reserved_places")
    private Integer occupiedPlaces;

}
