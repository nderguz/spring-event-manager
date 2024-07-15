package org.example.eventmanager.events.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.eventmanager.events.domain.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventDtoMapper eventDtoMapper;

    @PostMapping()
    public ResponseEntity<EventDto> createEvent(
            @RequestBody @Valid EventCreateRequestDto eventCreateRequestDto
    ){
        var event = eventService.createEvent(eventCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(eventDtoMapper.toDto(event));
    }
}
