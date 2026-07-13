package com.example.eventpublisher.repository;

import com.example.eventpublisher.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}