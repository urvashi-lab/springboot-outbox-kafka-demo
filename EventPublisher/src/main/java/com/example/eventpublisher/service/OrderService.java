package com.example.eventpublisher.service;

import com.example.eventpublisher.dto.OrderCreatedEvent;
import com.example.eventpublisher.entity.Order;
import com.example.eventpublisher.entity.OutboxEvent;
import com.example.eventpublisher.repository.OrderRepository;
import com.example.eventpublisher.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository,
                        OutboxEventRepository outboxRepository,
                        ObjectMapper objectMapper) {

        this.orderRepository = orderRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }
    @Transactional
    public Order createOrder(Order order) {

        Order savedOrder = orderRepository.save(order);

        // Event DTO
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(savedOrder.getId());
        event.setProduct(savedOrder.getProduct());
        event.setQuantity(savedOrder.getQuantity());
        event.setCustomerName(savedOrder.getCustomerName());
        event.setCustomerEmail(savedOrder.getCustomerEmail());

        try {
            String payload = objectMapper.writeValueAsString(event);

            // Outbox entity
            OutboxEvent outbox = new OutboxEvent();
            outbox.setEventType("ORDER_CREATED");
            outbox.setPayload(payload);
            outbox.setPublished(false);

            outboxRepository.save(outbox);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event", e);
        }

        return savedOrder;
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<OutboxEvent> getUnpublishedEvents() {
        return outboxRepository.findByPublishedFalse();
    }
}