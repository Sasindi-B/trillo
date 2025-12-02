package com.trillo.app.dtos;

import java.time.Instant;

public class HotspotResponse {
    private String id;
    private String businessName;
    private String category;
    private String location;
    private String previewImage;
    private String businessOwnerId;
    private Instant createdAt;

    public HotspotResponse(String id, String businessName, String category, String location, String previewImage,
            String businessOwnerId, Instant createdAt) {
        this.id = id;
        this.businessName = businessName;
        this.category = category;
        this.location = location;
        this.previewImage = previewImage;
        this.businessOwnerId = businessOwnerId;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public String getBusinessOwnerId() {
        return businessOwnerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
