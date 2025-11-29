package com.trillo.innovesync.businessowner;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BusinessOwnerService {

    private final BusinessOwnerRepository repository;
    private final PasswordEncoder passwordEncoder;

    public BusinessOwnerService(BusinessOwnerRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = Objects.requireNonNull(repository, "repository");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder");
    }

    public BusinessOwnerSignupResponse signup(BusinessOwnerSignupRequest request) {
        validatePasswords(request.getPassword(), request.getConfirmPassword());
        String normalizedEmail = normalizeEmail(request.getEmail());
        if (repository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new IllegalArgumentException("Email already registered");
        }

        BusinessOwner owner = new BusinessOwner(
                request.getBusinessName().trim(),
                normalizedEmail,
                request.getLocation().trim(),
                safeTrim(request.getCategory()),
                passwordEncoder.encode(request.getPassword()));

        BusinessOwner saved = repository.save(owner);
        return new BusinessOwnerSignupResponse(
                saved.getId(),
                saved.getBusinessName(),
                saved.getEmail(),
                saved.getLocation(),
                saved.getCategory());
    }

    public Optional<BusinessOwner> authenticate(String email, String rawPassword) {
        if (email == null || rawPassword == null) {
            return Optional.empty();
        }
        return repository.findByEmailIgnoreCase(normalizeEmail(email))
                .filter(owner -> passwordEncoder.matches(rawPassword, owner.getPasswordHash()));
    }

    public BusinessOwnerSettingsResponse getSettings(String idOrEmail) {
        BusinessOwner owner = findByIdOrEmail(idOrEmail);
        return BusinessOwnerSettingsResponse.from(owner.getSettings());
    }

    public BusinessOwnerSettingsResponse updateSettings(String idOrEmail, BusinessOwnerSettingsRequest request) {
        Objects.requireNonNull(request, "request");
        BusinessOwner owner = findByIdOrEmail(idOrEmail);
        BusinessOwnerSettings updated = mapRequestToSettings(request);
        owner.setSettings(updated);
        repository.save(owner);
        return BusinessOwnerSettingsResponse.from(updated);
    }

    private void validatePasswords(String password, String confirmPassword) {
        if (!Objects.equals(password, confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private BusinessOwner findByIdOrEmail(String idOrEmail) {
        if (idOrEmail == null || idOrEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Business owner id or email is required");
        }
        String trimmed = idOrEmail.trim();
        return (trimmed.contains("@")
                ? repository.findByEmailIgnoreCase(normalizeEmail(trimmed))
                : repository.findById(trimmed))
                .orElseThrow(() -> new IllegalArgumentException("Business owner not found: " + trimmed));
    }

    private BusinessOwnerSettings mapRequestToSettings(BusinessOwnerSettingsRequest request) {
        BusinessOwnerSettings settings = new BusinessOwnerSettings();
        settings.setEmailAlertsEnabled(request.getEmailAlertsEnabled());
        settings.setSmsAlertsEnabled(request.getSmsAlertsEnabled());
        settings.setPushNotificationsEnabled(request.getPushNotificationsEnabled());
        settings.setDigestCadence(request.getDigestCadence().trim());
        settings.setProfileVisibility(request.getProfileVisibility().trim());
        settings.setDataSharingEnabled(request.getDataSharingEnabled());
        settings.setAutoSaveTripsEnabled(request.getAutoSaveTripsEnabled());
        settings.setLanguage(request.getLanguage().trim());
        settings.setCurrency(request.getCurrency().trim());
        settings.setTimezone(request.getTimezone().trim());
        settings.setTravelVibes(request.getTravelVibes());
        return settings;
    }
}
