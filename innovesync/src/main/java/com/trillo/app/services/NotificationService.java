package com.trillo.app.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trillo.app.dtos.NotificationRequest;
import com.trillo.app.dtos.NotificationResponse;
import com.trillo.app.entities.Notification;
import com.trillo.app.entities.NotificationType;
import com.trillo.app.repositories.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationResponse create(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notificationRepository.save(notification);
        return toResponse(notification);
    }

    public void notifyUser(String userId, String title, String message, NotificationType type) {
        notificationRepository.save(buildNotification(userId, title, message, type));
    }

    public List<NotificationResponse> list(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public NotificationResponse markRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
        return toResponse(notification);
    }

    private Notification buildNotification(String userId, String title, String message, NotificationType type) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setTitle(title);
        n.setMessage(message);
        n.setType(type);
        return n;
    }

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getUserId(),
                n.getTitle(),
                n.getMessage(),
                n.getType(),
                n.isRead(),
                n.getCreatedAt());
    }
}
