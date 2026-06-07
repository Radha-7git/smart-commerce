package com.radha.smartcommerce.kafka.consumer;

import com.radha.smartcommerce.audit.entity.AuditAction;
import com.radha.smartcommerce.audit.service.AuditService;
import com.radha.smartcommerce.common.constants.KafkaTopics;
import com.radha.smartcommerce.kafka.event.OrderCancelledEvent;
import com.radha.smartcommerce.kafka.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditEventConsumer {
    private final AuditService auditService;
    @KafkaListener (topics = KafkaTopics.ORDER_CREATED, groupId = "audit-group")
    public void auditOrderCreated (OrderCreatedEvent event) {
        auditService.log(event.userId(), AuditAction.ORDER_CREATED, "Order #" + event.orderId() +
                " created with amount " + event.totalAmount());
        log.info(
                "Audit log saved for order creation"
        );
    }
    @KafkaListener (topics = KafkaTopics.ORDER_CANCELLED, groupId = "audit-group")
    public void auditOrderCancelled (OrderCancelledEvent event) {
        auditService.log(event.userId(), AuditAction.ORDER_CANCELLED, "Order #" + event.orderId() +
                "cancelled");
        log.info(
                "Audit log saved for order cancellation"
        );
    }
}
