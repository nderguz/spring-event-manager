package org.example.eventmanager.events.model.event;


import jakarta.persistence.*;
import lombok.*;
import org.example.eventmanager.events.model.EventStatus;
import org.example.eventmanager.events.model.registration.RegistrationEntity;
import java.time.LocalDateTime;
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
    private Integer cost;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistrationEntity> registrations;

    @Override
    public String toString() {
        return "EventEntity{" +
                "id=" + id +
                ", locationId=" + locationId +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", ownerId=" + ownerId +
                ", maxPlaces=" + maxPlaces +
                ", cost=" + cost +
                ", duration=" + duration +
                ", date=" + date +
                ", registrations=" + registrations +
                '}';
    }
}
