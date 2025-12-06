package com.trillo.innovesync.membership;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Membership")
public class Membership {

    @Id
    private String id;

    @Indexed
    private String businessOwnerId;

    private MembershipPackage packageType;
    private List<MembershipFeature> features;
    private Instant startDate;
    private Instant expiryDate;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;

    protected Membership() {
        // For MongoDB mapper
    }

    public Membership(String businessOwnerId, MembershipPackage packageType, Instant startDate, Instant expiryDate) {
        this.businessOwnerId = businessOwnerId;
        this.packageType = packageType;
        this.features = new ArrayList<>(packageType.getFeatures());
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.isActive = true;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public String getId() {
        return id;
    }

    public String getBusinessOwnerId() {
        return businessOwnerId;
    }

    public void setBusinessOwnerId(String businessOwnerId) {
        this.businessOwnerId = businessOwnerId;
    }

    public MembershipPackage getPackageType() {
        return packageType;
    }

    public void setPackageType(MembershipPackage packageType) {
        this.packageType = packageType;
        // Update features when package type changes
        if (packageType != null) {
            this.features = new ArrayList<>(packageType.getFeatures());
        }
        this.updatedAt = Instant.now();
    }

    public List<MembershipFeature> getFeatures() {
        if (features == null) {
            return List.of();
        }
        return Collections.unmodifiableList(features);
    }

    public void setFeatures(List<MembershipFeature> features) {
        this.features = features == null ? new ArrayList<>() : new ArrayList<>(features);
        this.updatedAt = Instant.now();
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
        this.updatedAt = Instant.now();
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
        this.updatedAt = Instant.now();
    }

    public boolean isActive() {
        return isActive && expiryDate != null && expiryDate.isAfter(Instant.now());
    }

    public void setActive(boolean active) {
        this.isActive = active;
        this.updatedAt = Instant.now();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void touchUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    public void extendMembership(int days) {
        if (expiryDate == null) {
            this.expiryDate = Instant.now().plusSeconds(days * 24L * 60 * 60);
        } else {
            // Extend from current expiry date or now, whichever is later
            Instant baseDate = expiryDate.isAfter(Instant.now()) ? expiryDate : Instant.now();
            this.expiryDate = baseDate.plusSeconds(days * 24L * 60 * 60);
        }
        this.updatedAt = Instant.now();
    }
}
