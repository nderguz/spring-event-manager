package org.example.eventmanager.events.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id; //id события

    @Column(name = "location_id")
    private Long locationId; // id локации из таблицы locations

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private String status;

    @Column(name = "user_id")
    private Long ownerId;

    @Column(name = "max_places")
    private Integer maxPlaces;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    @Column(name = "reserved_places")
    private Integer occupiedPlaces;

}
