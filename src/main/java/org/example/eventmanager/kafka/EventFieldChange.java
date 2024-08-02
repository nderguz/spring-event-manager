package org.example.eventmanager.kafka;

import org.springframework.stereotype.Component;

@Component
public class EventFieldChange<T> {
    private T oldField;
    private T newField;
}
