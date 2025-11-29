package com.trillo.innovesync.businessowner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Embedded settings document for business owner preferences.
 */
public class BusinessOwnerSettings {
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

    public static BusinessOwnerSettings defaultSettings() {
        BusinessOwnerSettings settings = new BusinessOwnerSettings();
        settings.emailAlertsEnabled = true;
        settings.smsAlertsEnabled = false;
        settings.pushNotificationsEnabled = true;
        settings.digestCadence = "Weekly";
        settings.profileVisibility = "Travellers only";
        settings.dataSharingEnabled = true;
        settings.autoSaveTripsEnabled = true;
        settings.language = "English";
        settings.currency = "LKR - Sri Lankan Rupee";
        settings.timezone = "GMT+5:30 | Colombo";
        settings.travelVibes = List.of("Adventure ready");
        return settings;
    }

    public BusinessOwnerSettings copy() {
        BusinessOwnerSettings copy = new BusinessOwnerSettings();
        copy.emailAlertsEnabled = this.emailAlertsEnabled;
        copy.smsAlertsEnabled = this.smsAlertsEnabled;
        copy.pushNotificationsEnabled = this.pushNotificationsEnabled;
        copy.digestCadence = this.digestCadence;
        copy.profileVisibility = this.profileVisibility;
        copy.dataSharingEnabled = this.dataSharingEnabled;
        copy.autoSaveTripsEnabled = this.autoSaveTripsEnabled;
        copy.language = this.language;
        copy.currency = this.currency;
        copy.timezone = this.timezone;
        copy.travelVibes = new ArrayList<>(this.getTravelVibes());
        return copy;
    }

    public boolean isEmailAlertsEnabled() {
        return emailAlertsEnabled;
    }

    public void setEmailAlertsEnabled(boolean emailAlertsEnabled) {
        this.emailAlertsEnabled = emailAlertsEnabled;
    }

    public boolean isSmsAlertsEnabled() {
        return smsAlertsEnabled;
    }

    public void setSmsAlertsEnabled(boolean smsAlertsEnabled) {
        this.smsAlertsEnabled = smsAlertsEnabled;
    }

    public boolean isPushNotificationsEnabled() {
        return pushNotificationsEnabled;
    }

    public void setPushNotificationsEnabled(boolean pushNotificationsEnabled) {
        this.pushNotificationsEnabled = pushNotificationsEnabled;
    }

    public String getDigestCadence() {
        return digestCadence;
    }

    public void setDigestCadence(String digestCadence) {
        this.digestCadence = digestCadence;
    }

    public String getProfileVisibility() {
        return profileVisibility;
    }

    public void setProfileVisibility(String profileVisibility) {
        this.profileVisibility = profileVisibility;
    }

    public boolean isDataSharingEnabled() {
        return dataSharingEnabled;
    }

    public void setDataSharingEnabled(boolean dataSharingEnabled) {
        this.dataSharingEnabled = dataSharingEnabled;
    }

    public boolean isAutoSaveTripsEnabled() {
        return autoSaveTripsEnabled;
    }

    public void setAutoSaveTripsEnabled(boolean autoSaveTripsEnabled) {
        this.autoSaveTripsEnabled = autoSaveTripsEnabled;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public List<String> getTravelVibes() {
        if (travelVibes == null) {
            return List.of();
        }
        return Collections.unmodifiableList(travelVibes);
    }

    public void setTravelVibes(List<String> travelVibes) {
        if (travelVibes == null) {
            this.travelVibes = new ArrayList<>();
        } else {
            this.travelVibes = new ArrayList<>(travelVibes);
        }
    }
}
