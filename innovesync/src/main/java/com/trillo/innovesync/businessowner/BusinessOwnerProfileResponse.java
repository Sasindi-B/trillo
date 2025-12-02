package com.trillo.innovesync.businessowner;

import java.util.List;

public class BusinessOwnerProfileResponse {
    private String profileId;
    private String ownerId;
    private String businessName;
    private String email;
    private String location;
    private String category;
    private String description;
    private String googleMapsUrl;
    private List<String> imageUrls;
    private int imageCount;

    public BusinessOwnerProfileResponse(String profileId,
            String ownerId,
            String businessName,
            String email,
            String location,
            String category,
            String description,
            String googleMapsUrl,
            List<String> imageUrls) {
        this.profileId = profileId;
        this.ownerId = ownerId;
        this.businessName = businessName;
        this.email = email;
        this.location = location;
        this.category = category;
        this.description = description;
        this.googleMapsUrl = googleMapsUrl;
        this.imageUrls = imageUrls;
        this.imageCount = imageUrls == null ? 0 : imageUrls.size();
    }

    public String getProfileId() {
        return profileId;
    }

    public String getOwnerId() {
        return ownerId;
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

    public String getDescription() {
        return description;
    }

    public String getGoogleMapsUrl() {
        return googleMapsUrl;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public int getImageCount() {
        return imageCount;
    }
}
