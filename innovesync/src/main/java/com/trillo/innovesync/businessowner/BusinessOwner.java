package com.trillo.innovesync.businessowner;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "BusinessOwner")
public class BusinessOwner {

    @Id
    private String id;
    private String businessName;
    @Indexed(unique = true)
    private String email;
    private String location;
    private String category;
    private String passwordHash;
    private Instant createdAt;
    private BusinessOwnerSettings settings;

    protected BusinessOwner() {
        // For MongoDB mapper
    }

    public BusinessOwner(String businessName, String email, String location, String category, String passwordHash) {
        this.businessName = businessName;
        this.email = email;
        this.location = location;
        this.category = category;
        this.passwordHash = passwordHash;
        this.createdAt = Instant.now();
        this.settings = BusinessOwnerSettings.defaultSettings();
    }

    public String getId() {
        return id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public BusinessOwnerSettings getSettings() {
        if (settings == null) {
            settings = BusinessOwnerSettings.defaultSettings();
        }
        return settings;
    }

    public void setSettings(BusinessOwnerSettings settings) {
        this.settings = settings == null ? BusinessOwnerSettings.defaultSettings() : settings;
    }
}
