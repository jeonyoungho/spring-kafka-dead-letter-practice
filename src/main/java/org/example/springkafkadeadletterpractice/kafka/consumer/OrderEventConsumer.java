package org.example.springkafkadeadletterpractice.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.springkafkadeadletterpractice.domain.order.OrderEvent;
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
        String key = consumerRecord.key();
        KafkaOrderEventDto value = consumerRecord.value();
        log.info("[KAFKA][CONSUMER] topic = {}, key = {}, value = {}", topic, key, value);

        // 컨슈머 메시지 처리 실패시 메시지 처리 지연 현상 발생
        throw new RuntimeException();

//        orderEventHandler.handle(value);
//        acknowledgment.acknowledge();
    }
}
