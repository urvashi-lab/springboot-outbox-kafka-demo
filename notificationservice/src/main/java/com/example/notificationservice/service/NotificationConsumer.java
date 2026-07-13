package com.example.notificationservice.service;


import com.example.notificationservice.dto.OrderCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotificationConsumer {
    private final ObjectMapper objectMapper;

    private final EmailService emailService;

    public NotificationConsumer(ObjectMapper objectMapper,
                                EmailService emailService) {
        this.objectMapper = objectMapper;
        this.emailService = emailService;
    }
    @KafkaListener(
            topics = "order-events",
            groupId = "notification-group"
    )
    public void consume(String message) throws JsonProcessingException {
        try {
            OrderCreatedEvent event =
                    objectMapper.readValue(message, OrderCreatedEvent.class);

            System.out.println("Order ID: " + event.getOrderId());
            System.out.println("Customer: " + event.getCustomerName());
            System.out.println("Email: " + event.getCustomerEmail());
            System.out.println("Product: " + event.getProduct());
            emailService.sendOrderConfirmation(event);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

}
