package org.example.springkafkadeadletterpractice.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, Object data) {
        log.info("[Kafka][Producer] Topic: {}, Data: {} ", topic, data);
        kafkaTemplate.send(topic, data);
    }

}
