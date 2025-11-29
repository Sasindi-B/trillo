package com.trillo.innovesync.traveller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TravellerService {

    private final TravellerRepository travellerRepository;
    private final PasswordEncoder passwordEncoder;

    public TravellerService(TravellerRepository travellerRepository, PasswordEncoder passwordEncoder) {
        this.travellerRepository = Objects.requireNonNull(travellerRepository, "travellerRepository");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder");
    }

    public TravellerSignupResponse signup(TravellerSignupRequest request) {
        validatePasswords(request.getPassword(), request.getConfirmPassword());
        String normalizedEmail = normalizeEmail(request.getEmail());
        if (travellerRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new IllegalArgumentException("Email already registered");
        }

        List<String> interests = toInterestList(request.getInterests());
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        Traveller traveller = new Traveller(
                request.getFullName().trim(),
                normalizedEmail,
                request.getCity().trim(),
                interests,
                hashedPassword);

        Traveller saved = travellerRepository.save(traveller);
        return new TravellerSignupResponse(
                saved.getId(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getCity(),
                saved.getInterests());
    }

    public java.util.Optional<Traveller> authenticate(String email, String rawPassword) {
        if (email == null || rawPassword == null) {
            return java.util.Optional.empty();
        }
        return travellerRepository.findByEmailIgnoreCase(normalizeEmail(email))
                .filter(t -> passwordEncoder.matches(rawPassword, t.getPasswordHash()));
    }

    public TravellerSettingsResponse getSettings(String idOrEmail) {
        Traveller traveller = findByIdOrEmail(idOrEmail);
        return TravellerSettingsResponse.from(traveller.getSettings());
    }

    public TravellerSettingsResponse updateSettings(String idOrEmail, TravellerSettingsRequest request) {
        Objects.requireNonNull(request, "request");
        Traveller traveller = findByIdOrEmail(idOrEmail);
        TravellerSettings updated = mapRequestToSettings(request);
        traveller.setSettings(updated);
        travellerRepository.save(traveller);
        return TravellerSettingsResponse.from(updated);
    }

    private void validatePasswords(String password, String confirmPassword) {
        if (!Objects.equals(password, confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }

    private List<String> toInterestList(String interests) {
        if (interests == null || interests.trim().isEmpty()) {
            return List.of();
        }
        List<String> cleaned = new ArrayList<>();
        for (String part : interests.split(",")) {
            if (part == null) {
                continue;
            }
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                cleaned.add(trimmed);
            }
        }
        return cleaned;
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private Traveller findByIdOrEmail(String idOrEmail) {
        if (idOrEmail == null || idOrEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Traveller id or email is required");
        }
        String trimmed = idOrEmail.trim();
        return (trimmed.contains("@")
                ? travellerRepository.findByEmailIgnoreCase(normalizeEmail(trimmed))
                : travellerRepository.findById(trimmed))
                .orElseThrow(() -> new IllegalArgumentException("Traveller not found: " + trimmed));
    }

    private TravellerSettings mapRequestToSettings(TravellerSettingsRequest request) {
        TravellerSettings settings = new TravellerSettings();
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
