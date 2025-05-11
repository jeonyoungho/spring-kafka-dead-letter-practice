package org.example.springkafkadeadletterpractice.kafka.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.springkafkadeadletterpractice.domain.order.OrderEvent;
import org.example.springkafkadeadletterpractice.domain.order.OrderEventType;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class KafkaOrderEventDto implements Serializable {
    private Long orderId;
    private OrderEventType eventType;

    public static KafkaOrderEventDto from(OrderEvent event) {
        return new KafkaOrderEventDto(event.getOrderId(), event.getEventType());
    }
}
