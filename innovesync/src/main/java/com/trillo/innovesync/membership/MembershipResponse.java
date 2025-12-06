package com.trillo.innovesync.membership;

import java.time.Instant;
import java.util.List;

public class MembershipResponse {
    private String id;
    private String businessOwnerId;
    private String packageType;
    private String displayName;
    private List<String> features;
    private Instant startDate;
    private Instant expiryDate;
    private boolean isActive;
    private Instant createdAt;

    public MembershipResponse() {
    }

    public MembershipResponse(Membership membership) {
        this.id = membership.getId();
        this.businessOwnerId = membership.getBusinessOwnerId();
        this.packageType = membership.getPackageType().name();
        this.displayName = membership.getPackageType().getDisplayName();
        this.features = membership.getFeatures().stream()
                .map(MembershipFeature::getDisplayName)
                .toList();
        this.startDate = membership.getStartDate();
        this.expiryDate = membership.getExpiryDate();
        this.isActive = membership.isActive();
        this.createdAt = membership.getCreatedAt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessOwnerId() {
        return businessOwnerId;
    }

    public void setBusinessOwnerId(String businessOwnerId) {
        this.businessOwnerId = businessOwnerId;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
