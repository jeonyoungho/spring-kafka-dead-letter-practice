package org.example.springkafkadeadletterpractice.domain.order;

import lombok.Getter;

@Getter
public class OrderCreateEvent extends OrderEvent {

    private OrderCreateEvent(Long orderId) {
        super(orderId, OrderEventType.CREATE);
    }

    public static OrderCreateEvent create(Long orderId) {
        return new OrderCreateEvent(orderId);
    }

}
