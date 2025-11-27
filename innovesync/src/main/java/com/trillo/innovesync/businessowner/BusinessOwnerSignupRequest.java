package com.trillo.innovesync.businessowner;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessOwnerSignupRequest {

    @JsonAlias({"businessName", "business_name", "name"})
    @NotBlank(message = "Business name is required")
    private String businessName;

    @JsonAlias({"email", "emailAddress", "email_address"})
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @JsonAlias({"location", "businessLocation", "business_location"})
    @NotBlank(message = "Location is required")
    private String location;

    @JsonAlias({"category", "categories", "businessCategory", "business_category"})
    private String category;

    @JsonAlias({"password", "pwd"})
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @JsonAlias({
            "confirmPassword",
            "confirm_password",
            "confirm",
            "passwordConfirm",
            "password_confirm",
            "passwordConfirmation",
            "password_confirmation",
            "confirmPwd",
            "confirm_pwd",
            "rePassword",
            "re_password"
    })
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    public BusinessOwnerSignupRequest() {
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
