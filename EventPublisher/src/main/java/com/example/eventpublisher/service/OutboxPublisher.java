package com.example.eventpublisher.service;

import com.example.eventpublisher.entity.OutboxEvent;
import com.example.eventpublisher.repository.OutboxEventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class OutboxPublisher {

    private final OutboxEventRepository repository;
    private final KafkaProducerService kafkaProducerService;

    public OutboxPublisher(KafkaProducerService kafkaProducerService, OutboxEventRepository repository) {
        this.kafkaProducerService = kafkaProducerService;
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 5000)
    public void publishEvents() {
        List<OutboxEvent> events =
                repository.findByPublishedFalse();
        for (OutboxEvent event : events) {

            try {
                System.out.println("Publishing event: " + event.getId());

                kafkaProducerService.publish(event.getPayload());

                event.setPublished(true);

                repository.save(event);

            } catch (Exception e) {

                System.out.println("Publishing failed. Will retry later.");

            }

        }

    }

}