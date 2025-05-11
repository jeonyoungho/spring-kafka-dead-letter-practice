package org.example.springkafkadeadletterpractice.kafka.consumer;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.springkafkadeadletterpractice.domain.order.Order;
import org.example.springkafkadeadletterpractice.domain.order.OrderRepository;
import org.example.springkafkadeadletterpractice.domain.stock.Stock;
import org.example.springkafkadeadletterpractice.domain.stock.StockRepository;
import org.example.springkafkadeadletterpractice.kafka.dto.KafkaOrderEventDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderEventHandler {

    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void handle(KafkaOrderEventDto dto) {
        switch (dto.getEventType()) {
            case CREATE -> handleCreateEvent(dto);
        }
    }

    private void handleCreateEvent(KafkaOrderEventDto event) {
        Order order = findOrder(event.getOrderId());
        Stock stock = findStockByProductId(order.getProductId());
        stock.decreaseStock();
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                              .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    private Stock findStockByProductId(Long productId) {
        return stockRepository.findByProductId(productId)
                              .orElseThrow(() -> new EntityNotFoundException("Stock not found with product id: " + productId));
    }
}
