//package org.example.eventmanager.events.domain;
//
//import org.example.eventmanager.events.UniversalEventMapper;
//import org.example.eventmanager.events.db.EventEntity;
//import org.example.eventmanager.events.db.EventRepository;
//import org.example.eventmanager.events.db.RegistrationEntity;
//import org.example.eventmanager.events.db.RegistrationRepository;
//import org.example.eventmanager.security.entities.Roles;
//import org.example.eventmanager.security.services.AuthenticationService;
//import org.example.eventmanager.users.domain.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
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
//class EventRegistrationServiceTest {
//
//    @Mock
//    private AuthenticationService authenticationService;
//
//    @Mock
//    private EventRepository eventRepository;
//
//    @Mock
//    private RegistrationRepository registrationRepository;
//
//    @Mock
//    private UniversalEventMapper universalEventMapper;
//
//    @InjectMocks
//    private EventRegistrationService eventRegistrationService;
//
//    private User currentUser;
//    private EventEntity event;
//    private RegistrationEntity registration;
////
////    @BeforeEach
////    void setUp() {
////        currentUser = new User(1L, "user1", "passwordHash", 25, Roles.USER);
////        event = new EventEntity(1L,
////                1L, "Event 1",
////                EventStatus.WAITING,
////                1L,
////                100L,
////                new BigDecimal(100),
////                60,
////                ZonedDateTime.of(2024, 5, 5, 12, 0, 0, 0, ZoneId.of("UTC")),
////                ZonedDateTime.of(2024, 5, 5, 13, 0, 0, 0, ZoneId.of("UTC")),
////                null);
////        registration = new RegistrationEntity(1L, event, currentUser.getId(), RegistrationStatus.OPENED);
////    }
//
//    @Test
//    void registerUserToEvent_Success() {
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//        when(registrationRepository.findUserRegistration(anyLong(), anyLong())).thenReturn(Optional.empty());
//        when(eventRepository.getEventOpenedRegistrations(anyLong())).thenReturn(List.of());
//
//        eventRegistrationService.registerUserToEvent(event.getId());
//
//        verify(registrationRepository, times(1)).save(any(RegistrationEntity.class));
//    }
//
//    @Test
//    void registerUserToEvent_EventNotFound() {
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            eventRegistrationService.registerUserToEvent(event.getId());
//        });
//
//        assertEquals("Event not found by id: 1", exception.getMessage());
//    }
//
//    @Test
//    void registerUserToEvent_OwnerCannotRegister() {
//        event.setOwnerId(currentUser.getId());
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            eventRegistrationService.registerUserToEvent(event.getId());
//        });
//
//        assertEquals("Owner cannot register to the event", exception.getMessage());
//    }
//
//    @Test
//    void registerUserToEvent_EventFinishedOrCancelled() {
//        event.setStatus(EventStatus.FINISHED);
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            eventRegistrationService.registerUserToEvent(event.getId());
//        });
//
//        assertEquals("User cannot register to finished or cancelled event", exception.getMessage());
//    }
//
//    @Test
//    void registerUserToEvent_UserAlreadyRegistered() {
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//        when(registrationRepository.findUserRegistration(anyLong(), anyLong())).thenReturn(Optional.of(registration));
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            eventRegistrationService.registerUserToEvent(event.getId());
//        });
//
//        assertEquals("User already registered to this event", exception.getMessage());
//    }
//
//    @Test
//    void registerUserToEvent_EventCapacityExceeded() {
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//        when(registrationRepository.findUserRegistration(anyLong(), anyLong())).thenReturn(Optional.empty());
//        when(eventRepository.getEventOpenedRegistrations(anyLong())).thenReturn(List.of(new RegistrationEntity()));
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            eventRegistrationService.registerUserToEvent(event.getId());
//        });
//
//        assertEquals("The allowed guest limit for the event has been exceeded", exception.getMessage());
//    }
//
//    @Test
//    void cancelRegistration_Success() {
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//        when(registrationRepository.findUserRegistration(anyLong(), anyLong())).thenReturn(Optional.of(registration));
//
//        eventRegistrationService.cancelRegistration(event.getId());
//
//        verify(registrationRepository, times(1)).closeRegistration(anyLong(), any(EventEntity.class));
//    }
//
//    @Test
//    void cancelRegistration_EventNotFound() {
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            eventRegistrationService.cancelRegistration(event.getId());
//        });
//
//        assertEquals("Event not found by id: 1", exception.getMessage());
//    }
//
//    @Test
//    void cancelRegistration_EventStartedOrFinished() {
//        event.setStatus(EventStatus.STARTED);
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            eventRegistrationService.cancelRegistration(event.getId());
//        });
//
//        assertEquals("Cannot cancel registration at started or finished event", exception.getMessage());
//    }
//
//    @Test
//    void cancelRegistration_RegistrationNotFound() {
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//        when(registrationRepository.findUserRegistration(anyLong(), anyLong())).thenReturn(Optional.empty());
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            eventRegistrationService.cancelRegistration(event.getId());
//        });
//
//        assertEquals("Registration not found", exception.getMessage());
//    }
//
//    @Test
//    void getUserRegistrations_Success() {
//        when(authenticationService.getCurrentAuthenticatedUser()).thenReturn(currentUser);
//        when(registrationRepository.findUserEvents(anyLong())).thenReturn(List.of(event));
//        when(universalEventMapper.entityToDomain(any(EventEntity.class))).thenReturn(new EventDomain(List.of(), null, null, null, null, null, null, null, null, null));
//
//        List<EventDomain> registrations = eventRegistrationService.getUserRegistrations();
//
//        assertNotNull(registrations);
//        assertFalse(registrations.isEmpty());
//        verify(registrationRepository, times(1)).findUserEvents(anyLong());
//        verify(universalEventMapper, times(1)).entityToDomain(any(EventEntity.class));
//    }
//}