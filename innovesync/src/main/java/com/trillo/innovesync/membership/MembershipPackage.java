package com.trillo.innovesync.membership;

import java.math.BigDecimal;
import java.util.List;

public enum MembershipPackage {
    SILVER("Silver Membership", new BigDecimal("29.99"), List.of(
            MembershipFeature.LIMITED_ANALYTICS,
            MembershipFeature.UP_TO_X_IMAGES,
            MembershipFeature.LIMITED_PROMOTION_VISIBILITY,
            MembershipFeature.BASIC_CUSTOMER_ENGAGEMENT
    )),
    GOLD("Gold Membership", new BigDecimal("79.99"), List.of(
            MembershipFeature.UNLIMITED_ANALYTICS,
            MembershipFeature.UNLIMITED_IMAGES,
            MembershipFeature.PRIORITY_PROMOTION_VISIBILITY,
            MembershipFeature.FEATURED_LISTING,
            MembershipFeature.ADVANCED_ANALYTICS_DASHBOARD,
            MembershipFeature.DIRECT_MESSAGING_PRIORITY,
            MembershipFeature.BASIC_CUSTOMER_ENGAGEMENT
    ));

    private final String displayName;
    private final BigDecimal price;
    private final List<MembershipFeature> features;

    MembershipPackage(String displayName, BigDecimal price, List<MembershipFeature> features) {
        this.displayName = displayName;
        this.price = price;
        this.features = features;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<MembershipFeature> getFeatures() {
        return features;
    }

    public boolean isUpgradeFrom(MembershipPackage other) {
        if (other == null) {
            return true;
        }
        return this == GOLD && other == SILVER;
    }
}
