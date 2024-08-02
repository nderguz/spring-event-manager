package org.example.eventmanager.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaSender {

    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        log.info("Sending message: " + message);
        log.info("Topic: " + topic);
        log.info("----------------------------------------------------------");
        kafkaTemplate.send(topic, message);
    }
}
