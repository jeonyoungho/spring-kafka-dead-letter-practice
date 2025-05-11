package org.example.springkafkadeadletterpractice.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springkafkadeadletterpractice.domain.order.OrderEvent;
import org.example.springkafkadeadletterpractice.kafka.producer.KafkaProducer;
import org.example.springkafkadeadletterpractice.kafka.dto.KafkaOrderEventDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    @Value("${spring.kafka.topic.order-event}")
    private String orderEventTopic;

    private final KafkaProducer kafkaProducer;

    @TransactionalEventListener
    public void listen(OrderEvent event) {
        kafkaProducer.send(orderEventTopic, KafkaOrderEventDto.from(event));
    }
}
