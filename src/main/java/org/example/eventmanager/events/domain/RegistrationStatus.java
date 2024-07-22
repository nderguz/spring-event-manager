package org.example.eventmanager.events.domain;

public enum RegistrationStatus {
    //**
    // Пользователь зарегестрирован на мероприятие
    // */
    OPENED,
    //**
    // Пользователь закрыл регистрацию на мероприятие, и может зарегистрироваться снова
    // */
    CLOSED
}
