package org.example.springkafkadeadletterpractice.kafka.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.springkafkadeadletterpractice.kafka.exception.DeserializationException;
import org.example.springkafkadeadletterpractice.kafka.exception.SerializationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaJsonConverter {

    private final ObjectMapper objectMapper;

    public KafkaJsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String serialize(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            String errorMessage = String.format("[KafkaJsonConverter] Failed to serialize data: %s", data);
            throw new SerializationException(errorMessage, e);
        }
    }

    public <T> T deserialize(String data, Class<T> clazz) {
        try {
            return objectMapper.readValue(data, clazz);
        } catch (Exception e) {
            String errorMessage = String.format("[KafkaJsonConverter] Failed to deserialize data: %s", data);
            throw new DeserializationException(errorMessage, e);
        }
    }
}
