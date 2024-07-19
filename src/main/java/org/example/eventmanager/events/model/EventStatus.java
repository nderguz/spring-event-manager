package org.example.eventmanager.events.model;

import org.springframework.stereotype.Component;

public enum EventStatus {

    /**
     * Мероприятие началось
     * */
    STARTED,

    /**
     * Мероприятие закрыто
     * */
    CANCELLED,

    /**
     * Мероприятие окончено
     * */
    FINISHED,

    /**
     * Ожидание начала мероприятия
     * */
    WAITING
}
