//package org.example.eventmanager.events.domain;
//
//import jakarta.persistence.EntityNotFoundException;
//import org.example.eventmanager.events.UniversalEventMapper;
//import org.example.eventmanager.events.api.RequestEvent;
//import org.example.eventmanager.events.api.UpdateEvent;
//import org.example.eventmanager.events.db.EventEntity;
//import org.example.eventmanager.events.db.EventRepository;
//import org.example.eventmanager.events.db.RegistrationRepository;
//import org.example.eventmanager.integration.IntegrationTestBase;
//import org.example.eventmanager.location.domain.Location;
//import org.example.eventmanager.location.domain.LocationService;
//import org.example.eventmanager.security.entities.Roles;
//import org.example.eventmanager.security.services.AuthenticationService;
//import org.example.eventmanager.users.domain.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.authentication.BadCredentialsException;
//
//import java.math.BigDecimal;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class EventServiceTest extends IntegrationTestBase {
//
//    @Mock
//    private LocationService locationService;
//
//    @Mock
//    private EventRepository eventRepository;
//
//    @Mock
//    private UniversalEventMapper universalEventMapper;
//
//    @Mock
//    private AuthenticationService authenticationService;
//
//    @Mock
//    private RegistrationRepository registrationRepository;
//
//    @InjectMocks
//    private EventService eventService;
//
//    private User currentUser;
//    private EventEntity event;
//    private Location location;
//
//    @BeforeEach
//    void setUp() {
//        currentUser = new User(1L, "user1", "passwordHash", 25, Roles.USER);
//        location = new Location(1L, "Location 1", "Address 1", 100L, "Description");
//        event = new EventEntity(1L, 1L, "Event 1", EventStatus.WAITING, 1L, 100L, new BigDecimal(100), 60,
//                ZonedDateTime.of(2024, 5, 5, 13, 0, 0, 0, ZoneId.of("UTC")),
//                ZonedDateTime.of(2024, 5, 5, 15, 0, 0, 0, ZoneId.of("UTC")), List.of());
//    }
//
//    @Test
//    void createEvent_Success() {
//        RequestEvent requestEvent = new RequestEvent(
//                ZonedDateTime.of(2024, 5, 5, 13, 0, 0, 0, ZoneId.of("UTC")),
//                60,
//                new BigDecimal(100),
//                100L,
//                1L,
//                "Event 1"
//        );
//
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(locationService.getLocationById(anyLong())).thenReturn(location);
//        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);
//        when(universalEventMapper.entityToDomain(any(EventEntity.class))).thenReturn(new EventDomain(List.of(), null, null, null, null, null, null, null, null, null));
//
////        EventDomain createdEvent = eventService.createEvent(requestEvent);
//
//        assertNotNull(createdEvent);
//        verify(eventRepository, times(1)).save(any(EventEntity.class));
//    }
//
//    @Test
//    void createEvent_LocationCapacityExceeded() {
//        RequestEvent requestEvent = new RequestEvent(
//                ZonedDateTime.of(2024, 5, 5, 13, 0, 0, 0, ZoneId.of("UTC")),
//                60,
//                new BigDecimal(100),
//                200L,
//                1L,
//                "Event 1"
//        );
//
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(locationService.getLocationById(anyLong())).thenReturn(location);
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            eventService.createEvent(requestEvent);
//        });
//
//        assertEquals("Capacity of location is: 100, but maxPlaces is: 200", exception.getMessage());
//    }
//
//    @Test
//    void getEventById_Success() {
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//        when(universalEventMapper.entityToDomain(any(EventEntity.class))).thenReturn(new EventDomain(List.of(), null, null, null, null, null, null, null, null, null));
//
//        EventDomain foundEvent = eventService.getEventById(1L);
//
//        assertNotNull(foundEvent);
//        verify(eventRepository, times(1)).findById(anyLong());
//    }
//
//    @Test
//    void getEventById_NotFound() {
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
//            eventService.getEventById(1L);
//        });
//
//        assertEquals("Event not found by id: 1", exception.getMessage());
//    }
//
//    @Test
//    void deleteEvent_Success() {
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//        when(universalEventMapper.entityToDomain(any(EventEntity.class))).thenReturn(new EventDomain(List.of(), null, null, null, null, null, null, null, null, null));
//
//        EventDomain deletedEvent = eventService.deleteEvent(1L);
//
//        assertNotNull(deletedEvent);
//        verify(eventRepository, times(1)).changeEventStatus(anyLong(), any(EventStatus.class));
//        verify(registrationRepository, times(1)).closeAllRegistrations(any(EventEntity.class));
//    }
//
//    @Test
//    void deleteEvent_NotAuthorized() {
//        User anotherUser = new User(2L, "user2", "passwordHash", 30, Roles.USER);
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(anotherUser);
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//
//        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
//            eventService.deleteEvent(1L);
//        });
//
//        assertEquals("Данный пользователь не может удалить событие", exception.getMessage());
//    }
//
//    @Test
//    void updateEvent_Success() {
//        UpdateEvent updateEvent = new UpdateEvent(
//                ZonedDateTime.of(2024, 5, 6, 13, 0, 0, 0, ZoneId.of("UTC")),
//                90,
//                new BigDecimal(150),
//                150L,
//                1L,
//                "Updated Event"
//        );
//
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//        when(universalEventMapper.entityToDomain(any(EventEntity.class))).thenReturn(new EventDomain(List.of(), null, null, null, null, null, null, null, null, null));
//
//        EventDomain updatedEvent = eventService.updateEvent(1L, updateEvent);
//
//        assertNotNull(updatedEvent);
//        verify(eventRepository, times(1)).save(any(EventEntity.class));
//    }
//
//    @Test
//    void updateEvent_EventNotFound() {
//        UpdateEvent updateEvent = new UpdateEvent(
//                ZonedDateTime.of(2024, 5, 6, 13, 0, 0, 0, ZoneId.of("UTC")),
//                90,
//                new BigDecimal(150),
//                150L,
//                1L,
//                "Updated Event"
//        );
//
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            eventService.updateEvent(1L, updateEvent);
//        });
//
//        assertEquals("Event not found by id: 1", exception.getMessage());
//    }
//
//    @Test
//    void getUserEvents_Success() {
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(eventRepository.findAllUserEvents(anyLong())).thenReturn(List.of(event));
//        when(universalEventMapper.entityToDomain(any(EventEntity.class))).thenReturn(new EventDomain(List.of(), null, null, null, null, null, null, null, null, null));
//
//        List<EventDomain> userEvents = eventService.getUserEvents();
//
//        assertNotNull(userEvents);
//        assertFalse(userEvents.isEmpty());
//        verify(eventRepository, times(1)).findAllUserEvents(anyLong());
//    }
//
//}