package org.example.springkafkadeadletterpractice.kafka.consumer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.springkafkadeadletterpractice.kafka.dto.KafkaOrderEventDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderEventHandler orderEventHandler;

    @KafkaListener(topics = "${spring.kafka.topic.order-event}")
    public void consume(ConsumerRecord<String, KafkaOrderEventDto> consumerRecord,
                        Acknowledgment acknowledgment) {
        String topic = consumerRecord.topic();
        KafkaOrderEventDto value = consumerRecord.value();

        log.info("[Kafka][Consumer] Topic: {}, Partition: {},  Value:{}", topic, consumerRecord.partition(), value);

        throw new RuntimeException();

//        orderEventHandler.handle(value);
//        acknowledgment.acknowledge();
    }
}
