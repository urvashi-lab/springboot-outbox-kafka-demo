package com.example.eventpublisher.controller;

import com.example.eventpublisher.entity.Order;
import com.example.eventpublisher.entity.OutboxEvent;
import com.example.eventpublisher.service.KafkaProducerService;
import com.example.eventpublisher.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;
    private final KafkaProducerService kafkaProducerService;

//    public OrderController(OrderService service) {
//        this.service = service;
//    }


    public OrderController(KafkaProducerService kafkaProducerService, OrderService service) {
        this.kafkaProducerService = kafkaProducerService;
        this.service = service;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {

        return service.createOrder(order);

    }
    @GetMapping
    public List<Order> getOrders() {
        return service.getAllOrders();
    }
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return service.getOrderById(id);
    }

    @GetMapping("/events")
    public List<OutboxEvent> getEvents() {
        return service.getUnpublishedEvents();
    }

    @PostMapping("/kafka")
    public void testKafka() {
        kafkaProducerService.publish("Hello Kafka");

    }
}