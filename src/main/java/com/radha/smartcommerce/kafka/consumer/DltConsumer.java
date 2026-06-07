package com.radha.smartcommerce.kafka.consumer;

import com.radha.smartcommerce.common.constants.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DltConsumer {
    @KafkaListener(topics = KafkaTopics.ORDER_CREATED_DLT, groupId = "dlt-group")
    public void consumeOrderCreatedDlt(
            Object event
    ) {

        log.error(
                "Message moved to DLT: {}",
                event
        );
    }
}
