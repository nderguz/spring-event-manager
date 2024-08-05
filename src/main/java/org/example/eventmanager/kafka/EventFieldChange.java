package org.example.eventmanager.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EventFieldChange<T> {
    private T oldField;
    private T newField;
}
