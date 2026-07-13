package com.example.notificationservice.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @PostMapping
    public void receiveNotification(@RequestBody String body) {

        System.out.println("Received Event");

        System.out.println(body);

    }

}

