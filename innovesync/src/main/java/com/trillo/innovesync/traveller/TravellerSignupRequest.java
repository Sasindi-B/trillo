package com.trillo.innovesync.traveller;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TravellerSignupRequest {

    @JsonAlias({"fullName", "fullname", "full_name", "name"})
    @NotBlank(message = "Full name is required")
    private String fullName;

    @JsonAlias({"email", "emailAddress", "email_address"})
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @JsonAlias({"city", "location"})
    @NotBlank(message = "City is required")
    private String city;

    @JsonAlias({"interests", "interest", "tags"})
    private String interests;

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

    public TravellerSignupRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
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
