package com.trillo.innovesync.businessowner;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessOwnerImagesRequest {

    @NotEmpty(message = "Provide at least one image URL")
    @Size(max = 6, message = "Provide up to 6 image URLs at a time")
    private List<@NotBlank String> imageUrls;

    public List<String> getImageUrls() {
        return imageUrls;
    }
}
