package org.example.springkafkadeadletterpractice.kafka.consumer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeadLetterConsumer {

    @KafkaListener(topics = "${spring.kafka.topic.dead-letter}", errorHandler = "deadLetterErrorHandler")
    public void consume(ConsumerRecord<String, Object> consumerRecord,
                        @Header(KafkaHeaders.DLT_ORIGINAL_TOPIC) String originalTopic,
                        @Header(KafkaHeaders.DLT_ORIGINAL_PARTITION) int originalPartition,
                        @Header(KafkaHeaders.DLT_ORIGINAL_TIMESTAMP) long originalTimestamp,
                        @Header(KafkaHeaders.DLT_EXCEPTION_STACKTRACE) String originalExceptionStackTrace,
                        Acknowledgment acknowledgment) {
        Object value = consumerRecord.value();
        LocalDateTime originalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(originalTimestamp), ZoneId.systemDefault());
        log.error("[Kafka][Consumer] Consumed Dead Letter Message ==> Topic:{}, Partition:{}, DateTime:{}, Value:{}, ExceptionStackTrace:{}",
                  originalTopic,
                  originalPartition,
                  originalDateTime,
                  value,
                  originalExceptionStackTrace);

        // ...

        acknowledgment.acknowledge();
    }
}
