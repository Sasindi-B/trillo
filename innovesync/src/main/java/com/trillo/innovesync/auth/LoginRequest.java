package com.trillo.innovesync.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {

    @JsonAlias({"username", "userName"})
    private String username;

    @JsonAlias({"email", "Email", "EMAIL", "emailAddress", "email_address"})
    private String email;

    @JsonAlias({"password", "pwd"})
    @NotBlank(message = "Password is required")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String usernameOrEmail, String password) {
        this.username = usernameOrEmail;
        this.email = usernameOrEmail;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailAddress(String email) {
        this.email = email;
    }

    public void setEmail_address(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
