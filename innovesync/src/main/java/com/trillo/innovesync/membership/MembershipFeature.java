package com.trillo.innovesync.membership;

public enum MembershipFeature {
    LIMITED_ANALYTICS("Limited Monthly Analytics"),
    UNLIMITED_ANALYTICS("Unlimited Analytics"),
    UP_TO_X_IMAGES("Upload up to X Images"),
    UNLIMITED_IMAGES("Unlimited Images"),
    LIMITED_PROMOTION_VISIBILITY("Limited Promotion Visibility"),
    PRIORITY_PROMOTION_VISIBILITY("Priority Promotion Visibility"),
    FEATURED_LISTING("Featured Listing on Category Pages"),
    ADVANCED_ANALYTICS_DASHBOARD("Advanced Analytics Dashboard"),
    DIRECT_MESSAGING_PRIORITY("Direct Messaging Priority"),
    BASIC_CUSTOMER_ENGAGEMENT("Basic Customer Engagement Features");

    private final String displayName;

    MembershipFeature(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
