package com.trillo.innovesync.auth;

import org.springframework.stereotype.Service;

import com.trillo.innovesync.businessowner.BusinessOwnerService;
import com.trillo.innovesync.exception.InvalidCredentialsException;
import com.trillo.innovesync.traveller.TravellerService;

@Service
public class AuthService {

    private final UserAccountService userAccountService;
    private final TravellerService travellerService;
    private final BusinessOwnerService businessOwnerService;

    public AuthService(UserAccountService userAccountService,
            TravellerService travellerService,
            BusinessOwnerService businessOwnerService) {
        this.userAccountService = userAccountService;
        this.travellerService = travellerService;
        this.businessOwnerService = businessOwnerService;
    }

    public LoginResponse login(LoginRequest request) {
        if (request == null || request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new InvalidCredentialsException("Password is required");
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
                        account.getUsername(), // Use email as ID for built-in accounts
                        account.getUsername(), // email
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
                                    traveller.getId(), // Use MongoDB ID
                                    traveller.getEmail(),
                                    traveller.getFullName(),
                                    Role.TRAVELLER);
                        }))
                // Then business owners stored in MongoDB
                .or(() -> businessOwnerService.authenticate(username, request.getPassword())
                        .map(owner -> {
                            UserAccount principal = new UserAccount(
                                    owner.getEmail(),
                                    owner.getBusinessName(),
                                    owner.getPasswordHash(),
                                    Role.BUSINESS_OWNER);
                            return new LoginResponse(
                                    userAccountService.issueToken(principal),
                                    owner.getId(), // Use MongoDB ID
                                    owner.getEmail(),
                                    owner.getBusinessName(),
                                    Role.BUSINESS_OWNER);
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
