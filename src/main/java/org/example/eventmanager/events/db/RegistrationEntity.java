package org.example.eventmanager.events.db;

import jakarta.persistence.*;
import lombok.*;
import org.example.eventmanager.events.domain.RegistrationStatus;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Table(name = "registrations")
public class RegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus;
}
