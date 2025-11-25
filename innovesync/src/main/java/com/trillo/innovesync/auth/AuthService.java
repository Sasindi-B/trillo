package com.trillo.innovesync.auth;

import org.springframework.stereotype.Service;

import com.trillo.innovesync.exception.InvalidCredentialsException;
import com.trillo.innovesync.traveller.TravellerService;
import com.trillo.innovesync.auth.UserAccount;
import com.trillo.innovesync.auth.Role;

@Service
public class AuthService {

    private final UserAccountService userAccountService;
    private final TravellerService travellerService;

    public AuthService(UserAccountService userAccountService, TravellerService travellerService) {
        this.userAccountService = userAccountService;
        this.travellerService = travellerService;
    }

    public LoginResponse login(LoginRequest request) {
        if (request == null || request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new InvalidCredentialsException("Username or email is required");
        }
        String inputUsername = firstNonBlank(request.getUsername(), request.getEmail());
        if (inputUsername == null) {
            throw new InvalidCredentialsException("Username or email is required");
        }
        final String username = inputUsername.trim();
        // First, try built-in accounts
        return userAccountService.authenticate(username, request.getPassword())
                .map(account -> new LoginResponse(
                        userAccountService.issueToken(account),
                        account.getUsername(),
                        account.getDisplayName(),
                        account.getRole()))
                // If not found, try traveller accounts stored in MongoDB
                .or(() -> travellerService.authenticate(username, request.getPassword())
                        .map(traveller -> {
                            UserAccount principal = new UserAccount(
                                    traveller.getEmail(),
                                    traveller.getFullName(),
                                    traveller.getPasswordHash(),
                                    Role.TRAVELLER);
                            return new LoginResponse(
                                    userAccountService.issueToken(principal),
                                    traveller.getEmail(),
                                    traveller.getFullName(),
                                    Role.TRAVELLER);
                        }))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) {
                return v;
            }
        }
        return null;
    }
}
