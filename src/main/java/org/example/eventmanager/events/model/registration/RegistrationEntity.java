package org.example.eventmanager.events.model.registration;

import jakarta.persistence.*;
import lombok.*;
import org.example.eventmanager.events.model.RegistrationStatus;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Table(name = "registrations")
public class RegistrationEntity {

    //todo запилить отношения
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "event_id")
//    private EventEntity event;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "status")
    private RegistrationStatus registrationStatus;
}
