package com.radha.smartcommerce.kafka.producer;

import com.radha.smartcommerce.common.constants.KafkaTopics;
import com.radha.smartcommerce.kafka.event.LowStockEvent;
import com.radha.smartcommerce.kafka.event.OrderCancelledEvent;
import com.radha.smartcommerce.kafka.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        kafkaTemplate.send(KafkaTopics.ORDER_CREATED, event);
        log.info("Event sent : {}" , event);
    }
    public void sendOrderCancelledEvent(
            OrderCancelledEvent event
    ) {
        kafkaTemplate.send(KafkaTopics.ORDER_CANCELLED, event);
        log.info("Cancelled Event Sent: {}" , event);
    }
    public void sendLowStockEvent (LowStockEvent event) {
        kafkaTemplate.send(KafkaTopics.LOW_STOCK, event);
        log.info("Event sent: {}" , event);
    }
}
