package com.trillo.app.dtos;

import java.time.Instant;

import com.trillo.app.entities.NotificationType;

public class NotificationResponse {
    private String id;
    private String userId;
    private String title;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private Instant createdAt;

    public NotificationResponse(String id, String userId, String title, String message, NotificationType type,
            boolean isRead, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public boolean isRead() {
        return isRead;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
