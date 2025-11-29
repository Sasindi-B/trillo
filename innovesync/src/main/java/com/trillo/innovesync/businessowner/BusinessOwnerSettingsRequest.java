package com.trillo.innovesync.businessowner;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BusinessOwnerSettingsRequest {

    @NotNull
    private Boolean emailAlertsEnabled;
    @NotNull
    private Boolean smsAlertsEnabled;
    @NotNull
    private Boolean pushNotificationsEnabled;
    @NotBlank
    private String digestCadence;

    @NotBlank
    private String profileVisibility;
    @NotNull
    private Boolean dataSharingEnabled;
    @NotNull
    private Boolean autoSaveTripsEnabled;

    @NotBlank
    private String language;
    @NotBlank
    private String currency;
    @NotBlank
    private String timezone;
    @Size(max = 10, message = "Provide up to 10 vibe tags")
    private List<@NotBlank String> travelVibes;

    public Boolean getEmailAlertsEnabled() {
        return emailAlertsEnabled;
    }

    public Boolean getSmsAlertsEnabled() {
        return smsAlertsEnabled;
    }

    public Boolean getPushNotificationsEnabled() {
        return pushNotificationsEnabled;
    }

    public String getDigestCadence() {
        return digestCadence;
    }

    public String getProfileVisibility() {
        return profileVisibility;
    }

    public Boolean getDataSharingEnabled() {
        return dataSharingEnabled;
    }

    public Boolean getAutoSaveTripsEnabled() {
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
