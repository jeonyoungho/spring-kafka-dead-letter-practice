package org.example.springkafkadeadletterpractice.service.order;

import lombok.RequiredArgsConstructor;
import org.example.springkafkadeadletterpractice.config.Events;
import org.example.springkafkadeadletterpractice.domain.order.Order;
import org.example.springkafkadeadletterpractice.domain.order.OrderCreateEvent;
import org.example.springkafkadeadletterpractice.domain.order.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Long create(Long memberId, Long productId) {
        Order savedOrder = orderRepository.save(Order.create(memberId, productId));

        Long savedOrderId = savedOrder.getId();

        Events.raise(OrderCreateEvent.create(savedOrderId));

        return savedOrder.getId();
    }

}
