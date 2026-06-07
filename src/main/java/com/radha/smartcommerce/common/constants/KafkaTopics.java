package com.radha.smartcommerce.common.constants;

public final class KafkaTopics {

    private KafkaTopics() {}

    public static final String ORDER_CREATED = "order-created";
    public static final String ORDER_CANCELLED = "order-cancelled";
    public static final String LOW_STOCK = "low-stock";
    public static final String ORDER_CREATED_DLT = "order-created-dlt";
}