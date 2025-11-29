package com.trillo.innovesync.traveller;

import java.util.List;

public class TravellerSettingsResponse {
    private boolean emailAlertsEnabled;
    private boolean smsAlertsEnabled;
    private boolean pushNotificationsEnabled;
    private String digestCadence;

    private String profileVisibility;
    private boolean dataSharingEnabled;
    private boolean autoSaveTripsEnabled;

    private String language;
    private String currency;
    private String timezone;
    private List<String> travelVibes;

    public static TravellerSettingsResponse from(TravellerSettings settings) {
        TravellerSettingsResponse response = new TravellerSettingsResponse();
        response.emailAlertsEnabled = settings.isEmailAlertsEnabled();
        response.smsAlertsEnabled = settings.isSmsAlertsEnabled();
        response.pushNotificationsEnabled = settings.isPushNotificationsEnabled();
        response.digestCadence = settings.getDigestCadence();
        response.profileVisibility = settings.getProfileVisibility();
        response.dataSharingEnabled = settings.isDataSharingEnabled();
        response.autoSaveTripsEnabled = settings.isAutoSaveTripsEnabled();
        response.language = settings.getLanguage();
        response.currency = settings.getCurrency();
        response.timezone = settings.getTimezone();
        response.travelVibes = settings.getTravelVibes();
        return response;
    }

    public boolean isEmailAlertsEnabled() {
        return emailAlertsEnabled;
    }

    public boolean isSmsAlertsEnabled() {
        return smsAlertsEnabled;
    }

    public boolean isPushNotificationsEnabled() {
        return pushNotificationsEnabled;
    }

    public String getDigestCadence() {
        return digestCadence;
    }

    public String getProfileVisibility() {
        return profileVisibility;
    }

    public boolean isDataSharingEnabled() {
        return dataSharingEnabled;
    }

    public boolean isAutoSaveTripsEnabled() {
        return autoSaveTripsEnabled;
    }

    public String getLanguage() {
        return language;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTimezone() {
        return timezone;
    }

    public List<String> getTravelVibes() {
        return travelVibes;
    }
}
