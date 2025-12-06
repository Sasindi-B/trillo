package com.trillo.innovesync.membership;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseMembershipRequest {

    @NotBlank(message = "Business owner ID is required")
    private String businessOwnerId;

    @NotNull(message = "Package type is required")
    private MembershipPackage packageType;

    public PurchaseMembershipRequest() {
    }

    public PurchaseMembershipRequest(String businessOwnerId, MembershipPackage packageType) {
        this.businessOwnerId = businessOwnerId;
        this.packageType = packageType;
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
    }
}
