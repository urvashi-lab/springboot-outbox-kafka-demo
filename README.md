# Spring Boot Outbox Kafka Demo

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![Apache Kafka](https://img.shields.io/badge/Apache-Kafka-black)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)
![Maven](https://img.shields.io/badge/Maven-Build-red)

A production-inspired event-driven microservices application demonstrating the **Transactional Outbox Pattern** using **Spring Boot**, **Apache Kafka**, and **PostgreSQL**.

## Overview

This project simulates an order processing system consisting of two independent microservices:

- **Event Publisher Service** – Creates orders, stores events in an Outbox table, and publishes them to Kafka.
- **Notification Service** – Consumes Kafka events and sends email notifications.

The project ensures **reliable event delivery** even if Kafka is temporarily unavailable.

---

## Architecture

```text
                POST /orders
                      │
                      ▼
          Event Publisher Service
                      │
          Save Order + Outbox Event
                      │
             PostgreSQL Database
                      │
          Scheduled Outbox Publisher
                      │
                      ▼
              Kafka Topic (order-events)
                      │
                      ▼
          Notification Service
                      │
              Send Email Notification
```

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Kafka
- PostgreSQL
- Apache Kafka
- Docker & Docker Compose
- Kafka UI
- Maven

---

## Key Features

- Event-driven microservices
- Transactional Outbox Pattern
- Kafka Producer & Consumer
- Scheduled Event Publisher
- Email Notifications
- PostgreSQL persistence
- Dockerized infrastructure
- Kafka UI integration

---

## Project Structure

```
springboot-outbox-kafka-demo
│
├── EventPublisher/
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   └── dto
│
├── notificationservice/
│   ├── service
│   └── dto
│
└── docker-compose.yml
```

---

## Request Flow

1. Client sends a request to create an order.
2. Order is saved in PostgreSQL.
3. An Outbox Event is stored in the same transaction.
4. A scheduled publisher reads unpublished events.
5. Events are published to Kafka.
6. Notification Service consumes the event.
7. Email notification is sent to the customer.

---

## Why the Outbox Pattern?

Publishing directly to Kafka after saving the database can lead to inconsistent data.

Example:

- ✅ Order saved
- ❌ Kafka unavailable

Without the Outbox Pattern, the order exists but no event is published.

With the Outbox Pattern:

- Order and event are saved together.
- If Kafka is unavailable, the event remains in the Outbox table.
- The scheduler retries publishing later.
- Once successful, the event is marked as published.

This guarantees **eventual consistency**.

---

## Running the Project

### 1. Clone the repository

```bash
git clone https://github.com/urvashi-lab/springboot-outbox-kafka-demo.git
```

### 2. Start infrastructure

```bash
docker compose up -d
```

### 3. Start Event Publisher

Runs on:

```
http://localhost:8080
```

### 4. Start Notification Service

Runs on:

```
http://localhost:8081
```

### 5. Create an Order

```http
POST /orders
```

Example Request

```json
{
  "product": "Laptop",
  "quantity": 1,
  "customerName": "John",
  "customerEmail": "john@example.com"
}
```

---

## Failure Handling

### Kafka Down

- Order is saved.
- Event remains in the Outbox table.
- `published = false`
- Scheduler retries automatically when Kafka becomes available.

### Notification Service Down

- Kafka safely stores the event.
- Consumer processes it once the service is back online.

---

## Future Improvements

- Dead Letter Topic (DLT)
- Retry Backoff Strategy
- Idempotent Consumers
- Debezium CDC
- Prometheus & Grafana
- Kubernetes Deployment
- Integration Tests

---

## What I Learned

- Designing event-driven microservices
- Implementing the Transactional Outbox Pattern
- Kafka Producer/Consumer communication
- Eventual Consistency
- Reliable message delivery
- Docker-based local development

---

## Author

**Urvashi Patil**

If you found this project interesting, feel free to ⭐ the repository!
