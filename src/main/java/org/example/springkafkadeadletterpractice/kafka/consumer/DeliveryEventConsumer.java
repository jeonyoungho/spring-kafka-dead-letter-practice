package org.example.springkafkadeadletterpractice.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.springkafkadeadletterpractice.kafka.dto.KafkaOrderEventDto;
import org.example.springkafkadeadletterpractice.kafka.util.KafkaJsonConverter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryEventConsumer {

    private final KafkaJsonConverter kafkaJsonConverter;

    @KafkaListener(topics = "${spring.kafka.topic.delivery-event}")
    public void consume(ConsumerRecord<String, String> consumerRecord,
                        Acknowledgment acknowledgment) {
        String topic = consumerRecord.topic();
        KafkaOrderEventDto value = kafkaJsonConverter.deserialize(consumerRecord.value(), KafkaOrderEventDto.class);

        log.info("[Kafka][Consumer] Topic: {}, Partition: {},  Value:{}", topic, consumerRecord.partition(), value);

//        throw new RetryableException(new RuntimeException("Retryable error occurred!"));
        throw new IllegalStateException("Illegal state error occurred!");
    }
}
