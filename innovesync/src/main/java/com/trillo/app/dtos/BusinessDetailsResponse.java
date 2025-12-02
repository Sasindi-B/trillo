package com.trillo.app.dtos;

import java.util.List;

public class BusinessDetailsResponse {
    private String businessId;
    private String businessName;
    private String category;
    private String location;
    private String description;
    private String googleMapsUrl;
    private List<String> images;
    private String contactEmail;
    private String contactName;
    private List<String> freeTimeSlots;

    public BusinessDetailsResponse(String businessId, String businessName, String category, String location,
            String description, String googleMapsUrl, List<String> images, String contactEmail, String contactName,
            List<String> freeTimeSlots) {
        this.businessId = businessId;
        this.businessName = businessName;
        this.category = category;
        this.location = location;
        this.description = description;
        this.googleMapsUrl = googleMapsUrl;
        this.images = images;
        this.contactEmail = contactEmail;
        this.contactName = contactName;
        this.freeTimeSlots = freeTimeSlots;
    }

    public String getBusinessId() {
        return businessId;
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

    public String getDescription() {
        return description;
    }

    public String getGoogleMapsUrl() {
        return googleMapsUrl;
    }

    public List<String> getImages() {
        return images;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getContactName() {
        return contactName;
    }

    public List<String> getFreeTimeSlots() {
        return freeTimeSlots;
    }
}
