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
}
