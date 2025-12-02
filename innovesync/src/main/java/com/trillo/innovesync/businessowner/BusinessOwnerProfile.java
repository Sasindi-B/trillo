package com.trillo.innovesync.businessowner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "BusinessOwnerProfile")
public class BusinessOwnerProfile {

    @Id
    private String id;

    @Indexed(unique = true)
    private String ownerId;

    private String description;
    private String googleMapsUrl;
    private List<String> imageUrls;
    private Instant createdAt;
    private Instant updatedAt;

    public BusinessOwnerProfile() {
    }

    public BusinessOwnerProfile(String ownerId) {
        this.ownerId = ownerId;
        this.description = "";
        this.googleMapsUrl = "";
        this.imageUrls = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public String getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description.trim();
    }

    public String getGoogleMapsUrl() {
        return googleMapsUrl == null ? "" : googleMapsUrl;
    }

    public void setGoogleMapsUrl(String googleMapsUrl) {
        this.googleMapsUrl = googleMapsUrl == null ? "" : googleMapsUrl.trim();
    }

    public List<String> getImageUrls() {
        if (imageUrls == null) {
            return List.of();
        }
        return Collections.unmodifiableList(imageUrls);
    }

    public void setImageUrls(List<String> imageUrls) {
        if (imageUrls == null) {
            this.imageUrls = new ArrayList<>();
        } else {
            this.imageUrls = new ArrayList<>(imageUrls);
        }
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void touchUpdatedAt() {
        this.updatedAt = Instant.now();
        if (this.createdAt == null) {
            this.createdAt = this.updatedAt;
        }
    }
}
