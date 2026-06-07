package com.radha.smartcommerce.kafka.consumer;

import com.radha.smartcommerce.common.constants.KafkaTopics;
import com.radha.smartcommerce.kafka.event.LowStockEvent;
import com.radha.smartcommerce.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LowStockConsumer {
    private final MailService mailService;
    @KafkaListener(topics= KafkaTopics.LOW_STOCK, groupId = "inventory-group")
    public void handleLowStock (LowStockEvent event) {
        mailService.sendMail(
                "r44196189@gmail.com",
                "Low Stock Alert",
                """
                Product: %s

                Remaining Stock: %d

                Please restock soon.
                """
                        .formatted(
                                event.productName(),
                                event.stockRemaining()
                        )
        );
        log.info( "Low stock email sent for {}" ,event.productName());
    }
}
