package com.trillo.innovesync.membership;

import java.math.BigDecimal;
import java.util.List;

public class MembershipPackageResponse {
    private String type;
    private String displayName;
    private BigDecimal price;
    private List<String> features;

    public MembershipPackageResponse() {
    }

    public MembershipPackageResponse(MembershipPackage packageType) {
        this.type = packageType.name();
        this.displayName = packageType.getDisplayName();
        this.price = packageType.getPrice();
        this.features = packageType.getFeatures().stream()
                .map(MembershipFeature::getDisplayName)
                .toList();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}
