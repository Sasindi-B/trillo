package com.trillo.app.entities;

import java.time.Instant;
import java.util.UUID;

public class Image {
    private String id = UUID.randomUUID().toString();
    private String url;
    private Instant createdAt = Instant.now();

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
