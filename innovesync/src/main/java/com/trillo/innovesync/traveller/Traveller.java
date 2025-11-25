package com.trillo.innovesync.traveller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "travellers")
public class Traveller {

    @Id
    private String id;
    private String fullName;
    @Indexed(unique = true)
    private String email;
    private String city;
    private List<String> interests;
    private String passwordHash;
    private Instant createdAt;

    protected Traveller() {
        // For MongoDB mapper
    }

    public Traveller(String fullName, String email, String city, List<String> interests, String passwordHash) {
        this.fullName = fullName;
        this.email = email;
        this.city = city;
        this.interests = interests == null ? new ArrayList<>() : new ArrayList<>(interests);
        this.passwordHash = passwordHash;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public List<String> getInterests() {
        return Collections.unmodifiableList(interests);
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
