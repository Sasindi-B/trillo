package com.trillo.innovesync.auth;

import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {

    private final Map<String, UserAccount> accounts = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    public UserAccountService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder");
        register("traveller@innovecorp.com", "Traveller#2025", "Terry Traveller", Role.TRAVELLER);
        register("admin@innovecorp.com", "Admin#2025", "Anita CompanyAdmin", Role.COMPANY_ADMIN);
        register("owner@innovecorp.com", "Owner#2025", "Omar BusinessOwner", Role.BUSINESS_OWNER);
    }

    private void register(String username, String rawPassword, String displayName, Role role) {
        String normalized = normalize(username);
        String hashed = passwordEncoder.encode(rawPassword);
        accounts.put(normalized, new UserAccount(normalized, displayName, hashed, role));
    }

    public Optional<UserAccount> findByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(accounts.get(normalize(username)));
    }

    public Optional<UserAccount> authenticate(String username, String password) {
        return findByUsername(username)
                .filter(account -> passwordEncoder.matches(password, account.getPasswordHash()));
    }

    public String issueToken(UserAccount account) {
        String payload = account.getUsername() + ":" + account.getRole() + ":" + Instant.now().toEpochMilli() + ":" + secureRandom.nextLong();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    }

    private String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
