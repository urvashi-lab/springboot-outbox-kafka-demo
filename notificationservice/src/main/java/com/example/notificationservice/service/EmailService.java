package com.example.notificationservice.service;

import com.example.notificationservice.dto.OrderCreatedEvent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmation(OrderCreatedEvent event) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(event.getCustomerEmail());
        message.setSubject("Order Confirmation");

        message.setText(
                "Hi " + event.getCustomerName() + ",\n\n" +
                        "Your order has been placed successfully.\n\n" +
                        "Order ID: " + event.getOrderId() + "\n" +
                        "Product: " + event.getProduct() + "\n" +
                        "Quantity: " + event.getQuantity() + "\n\n" +
                        "Thank you for shopping with us!"
        );

        mailSender.send(message);

        System.out.println("Email sent to " + event.getCustomerEmail());
    }
}
