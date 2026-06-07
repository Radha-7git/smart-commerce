package com.radha.smartcommerce.kafka.consumer;

import com.radha.smartcommerce.auth.entity.User;
import com.radha.smartcommerce.auth.repository.UserRepository;
import com.radha.smartcommerce.common.constants.KafkaTopics;
import com.radha.smartcommerce.exception.ResourceNotFoundException;
import com.radha.smartcommerce.kafka.event.OrderCancelledEvent;
import com.radha.smartcommerce.kafka.event.OrderCreatedEvent;

import com.radha.smartcommerce.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEmailConsumer {
    private final UserRepository userRepository;
    private final MailService mailService;
    @KafkaListener(topics = KafkaTopics.ORDER_CREATED, groupId = "smart-commerce-group")
    public void consumeOrderCreatedEvent (OrderCreatedEvent event) {
        User user = userRepository.findById(event.userId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        mailService.sendMail(user.getEmail(), "Order confirmed", "Your order #" + event.orderId() + " has been placed successfully.");
        log.info("Confirmation email sent");
    }
    @KafkaListener(topics = KafkaTopics.ORDER_CANCELLED, groupId = "smart-commerce-group")
    public void consumerOrderCancelledEvent (OrderCancelledEvent event) {
        log.info("Received Cancelled Event: {}" ,event);
    }
}
