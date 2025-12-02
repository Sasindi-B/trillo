package com.trillo.innovesync.businessowner;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessOwnerProfileRequest {

    @Size(max = 500, message = "Description can be up to 500 characters")
    private String description;

    @Size(max = 2048, message = "Google Maps URL is too long")
    private String googleMapsUrl;

    @Size(max = 6, message = "Provide up to 6 image URLs")
    private List<@NotBlank String> imageUrls;

    public String getDescription() {
        return description;
    }

    public String getGoogleMapsUrl() {
        return googleMapsUrl;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
}
