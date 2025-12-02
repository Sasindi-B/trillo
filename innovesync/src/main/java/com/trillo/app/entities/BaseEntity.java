package com.trillo.app.entities;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public abstract class BaseEntity {
    @Id
    private String id = UUID.randomUUID().toString();

    @Field("created_at")
    private Instant createdAt = Instant.now();

    public String getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
