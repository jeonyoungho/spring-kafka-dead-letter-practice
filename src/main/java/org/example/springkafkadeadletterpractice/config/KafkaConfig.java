package org.example.springkafkadeadletterpractice.config;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.springkafkadeadletterpractice.kafka.consumer.DeadLetterConsumer;
import org.example.springkafkadeadletterpractice.kafka.exception.RetryableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.EndpointHandlerMethod;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Value("${spring.kafka.topic.order-event}")
    private String orderEventTopic;

    @Value("${spring.kafka.topic.stock-event}")
    private String stockEventTopic;

    @Value("${spring.kafka.topic.delivery-event}")
    private String deliveryEventTopic;

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        Map<String, Object> properties = Maps.newHashMap();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(properties));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String>
                kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();

        Map<String, Object> properties = Maps.newHashMap();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        properties.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 10000);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        kafkaListenerContainerFactory.getContainerProperties().setAckMode(AckMode.MANUAL);
        kafkaListenerContainerFactory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(properties));
        kafkaListenerContainerFactory.setCommonErrorHandler(defaultErrorHandler());

        return kafkaListenerContainerFactory;
    }

    @Bean
    public DefaultErrorHandler defaultErrorHandler() {
        return new DefaultErrorHandler(new FixedBackOff(0, 0));
    }

    @Bean
    public RetryTopicConfiguration retryableTopic(KafkaTemplate<String, String> kafkaTemplate) {
        return RetryTopicConfigurationBuilder.newInstance()
                                             .maxAttempts(3)
                                             .exponentialBackoff(1000L, 2, 10 * 1000L)
                                             .autoCreateTopics(true, 1, (short) 1)
                                             .includeTopics(List.of(orderEventTopic))
                                             .retryOn(List.of(RetryableException.class))
                                             .setTopicSuffixingStrategy(TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
                                             .dltProcessingFailureStrategy(DltStrategy.ALWAYS_RETRY_ON_ERROR)
                                             .dltHandlerMethod(new EndpointHandlerMethod(DeadLetterConsumer.class, "consume"))
                                             .create(kafkaTemplate);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = Maps.newHashMap();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic orderEventTopic() {
        return TopicBuilder.name(orderEventTopic)
                           .partitions(1)
                           .build();
    }

    @Bean
    public NewTopic stockEventTopic() {
        return TopicBuilder.name(stockEventTopic)
                           .partitions(1)
                           .build();
    }

    @Bean
    public NewTopic deliveryEventTopic() {
        return TopicBuilder.name(deliveryEventTopic)
                           .partitions(1)
                           .build();
    }

}
