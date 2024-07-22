package org.example.eventmanager.events.model;

public enum EventStatus {

    /**
     * Ожидание начала мероприятия
     * */
    WAITING,
    /**
     * Мероприятие началось
     * */
    STARTED,
    /**
     * Мероприятие окончено
     * */
    FINISHED,
    /**
     * Мероприятие закрыто
     * */
    CANCELLED
}
