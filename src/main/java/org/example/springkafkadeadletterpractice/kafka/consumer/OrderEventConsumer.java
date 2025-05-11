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

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(consumerRecord.timestamp()), ZoneId.systemDefault());
        log.info("[Kafka][Consumer] Consumed Order Event Message! Topic:{}, Partition:{}, DateTime:{}, Value:{}",
                 topic,
                 consumerRecord.partition(),
                 dateTime,
                 value);

        // 컨슈머 메시지 처리 실패시 메시지 처리 지연 현상 발생
        throw new RuntimeException();

//        orderEventHandler.handle(value);
//        acknowledgment.acknowledge();
    }
}
