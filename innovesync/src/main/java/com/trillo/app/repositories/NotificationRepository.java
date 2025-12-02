package com.trillo.app.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trillo.app.entities.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
}
