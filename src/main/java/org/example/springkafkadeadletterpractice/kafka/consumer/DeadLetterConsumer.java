package org.example.springkafkadeadletterpractice.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeadLetterConsumer {

    public void consume(ConsumerRecord<String, String> consumerRecord,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        @Header(KafkaHeaders.OFFSET) Long offset,
                        @Header(KafkaHeaders.EXCEPTION_MESSAGE) String exceptionMessage,
                        @Header(KafkaHeaders.EXCEPTION_STACKTRACE) String exceptionStacktrace,
                        @Header(KafkaHeaders.GROUP_ID) String groupId,
                        Acknowledgment acknowledgment) {
        String key = consumerRecord.key();
        String value = consumerRecord.value();

        log.error("[Kafka][Consumer] Consumed Dead Letter Message ==> Topic:{}, Key: {}, Partition:{}, Offset:{}, Value:{}, groupId:{}, exceptionMessage:{}",
                  topic,
                  key,
                  partition,
                  offset,
                  value,
                  groupId,
                  exceptionMessage);

        // ...

        acknowledgment.acknowledge();
    }
}
