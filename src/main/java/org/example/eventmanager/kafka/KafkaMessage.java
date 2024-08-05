package org.example.eventmanager.kafka;

import lombok.Data;

@Data
public class KafkaMessage {
    private String message;

    @Override
    public String toString() {
        return "KafkaMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
