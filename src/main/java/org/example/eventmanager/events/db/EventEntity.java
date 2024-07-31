package org.example.eventmanager.events.db;


import jakarta.persistence.*;
import lombok.*;
import org.example.eventmanager.events.domain.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status")
    private EventStatus status;

    @Column(name = "user_id")
    private Long ownerId;

    @Column(name = "max_places", nullable = false)
    private Integer maxPlaces;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "date_start", nullable = false)
    private ZonedDateTime dateStart;

    @Column(name = "date_end", nullable = false)
    private ZonedDateTime dateEnd;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistrationEntity> registrations;

}
