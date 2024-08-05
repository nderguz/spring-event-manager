package org.example.eventmanager.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
@Slf4j
@AllArgsConstructor
public class KafkaController {

    private final KafkaSender sender;

    @GetMapping("/hello")
    public void hello(){
        var msg = new KafkaMessage();
        msg.setMyMessage("Hello World");
        sender.sendMessage("Topic", msg );
        log.info("Message sent to Kafka");
    }
}
