package com.trillo.app.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trillo.app.dtos.NotificationRequest;
import com.trillo.app.dtos.NotificationResponse;
import com.trillo.app.services.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public NotificationResponse create(@Valid @RequestBody NotificationRequest request) {
        return notificationService.create(request);
    }

    @GetMapping("/{userId}")
    public List<NotificationResponse> list(@PathVariable String userId) {
        return notificationService.list(userId);
    }

    @PutMapping("/{notificationId}/read")
    public NotificationResponse markRead(@PathVariable String notificationId) {
        return notificationService.markRead(notificationId);
    }
}
