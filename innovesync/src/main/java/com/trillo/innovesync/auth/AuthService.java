package com.trillo.innovesync.auth;

import org.springframework.stereotype.Service;

import com.trillo.innovesync.exception.InvalidCredentialsException;

@Service
public class AuthService {

    private final UserAccountService userAccountService;

    public AuthService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    public LoginResponse login(LoginRequest request) {
        return userAccountService.authenticate(request.username(), request.password())
                .map(account -> new LoginResponse(
                        userAccountService.issueToken(account),
                        account.getUsername(),
                        account.getDisplayName(),
                        account.getRole()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));
    }
}
